package com.example.local_network_scanner.util

import com.example.local_network_scanner.vpn.PacketParser
import org.xbill.DNS.*
import java.nio.ByteBuffer

object PacketCraft {

    fun buildFakeDnsResponse(ipHeader: PacketParser.IpHeader, udpHeader: PacketParser.UdpHeader, dnsQuery: Message): ByteBuffer {
        val dnsResponse = Message(dnsQuery.header.id)
        dnsResponse.header.setFlag(Flags.QR.toInt())
        dnsResponse.addRecord(dnsQuery.question, Section.QUESTION)

        val nxDomainResponse = Message(dnsQuery.header.id)
        nxDomainResponse.header.rcode = Rcode.NXDOMAIN
        nxDomainResponse.header.setFlag(Flags.QR.toInt())
        val dnsResponseBytes = nxDomainResponse.toWire()

        val totalLength = 20 + 8 + dnsResponseBytes.size // IP header + UDP header + DNS payload
        val packet = ByteBuffer.allocate(totalLength)

        // IP Header
        packet.put(0x45.toByte()) // Version and IHL
        packet.put(0.toByte()) // DSCP/ECN
        packet.putShort(totalLength.toShort()) // Total Length
        packet.putShort(0) // Identification
        packet.putShort(0x4000.toShort()) // Flags and Fragment Offset
        packet.put(64.toByte()) // TTL
        packet.put(17.toByte()) // Protocol (UDP)
        packet.putShort(0) // Header Checksum (placeholder)
        packet.put(ipHeader.destinationIp.split(".").map { it.toInt().toByte() }.toByteArray()) // Source IP (original destination)
        packet.put(ipHeader.sourceIp.split(".").map { it.toInt().toByte() }.toByteArray()) // Destination IP (original source)

        // UDP Header
        val udpHeaderPos = 20
        packet.position(udpHeaderPos)
        packet.putShort(udpHeader.destinationPort.toShort()) // Source Port (original destination)
        packet.putShort(udpHeader.sourcePort.toShort()) // Destination Port (original source)
        packet.putShort((8 + dnsResponseBytes.size).toShort()) // Length
        packet.putShort(0) // Checksum (placeholder)

        // DNS Payload
        packet.put(dnsResponseBytes)

        // Calculate Checksums
        packet.position(10)
        val ipChecksum = calculateChecksum(packet.array(), 0, 20)
        packet.putShort(10, ipChecksum)

        // UDP checksum is optional in IPv4

        packet.position(0)
        return packet
    }

    private fun calculateChecksum(data: ByteArray, offset: Int, length: Int): Short {
        var sum = 0L
        var i = offset
        while (i < offset + length) {
            var word = ((data[i].toInt() and 0xFF) shl 8) or (data[i + 1].toInt() and 0xFF)
            sum += word
            if ((sum and 0xFFFF0000L) > 0) {
                sum = (sum and 0xFFFF) + 1
            }
            i += 2
        }
        return (sum.inv() and 0xFFFF).toShort()
    }
}
