package com.example.local_network_scanner.vpn

import java.nio.ByteBuffer

class PacketParser {

    data class IpHeader(val version: Int, val headerLength: Int, val protocol: Int, val sourceIp: String, val destinationIp: String)
    data class TcpHeader(val sourcePort: Int, val destinationPort: Int, val isSyn: Boolean)
    data class UdpHeader(val sourcePort: Int, val destinationPort: Int)

    fun parseIpHeader(packet: ByteBuffer): IpHeader {
        packet.position(0)
        val versionAndIhl = packet.get().toInt()
        val version = versionAndIhl ushr 4
        val ihl = versionAndIhl and 0x0F
        val headerLength = ihl * 4 // Header length in bytes

        // Skip to protocol
        packet.position(9)
        val protocol = packet.get().toInt() and 0xFF

        // Skip to IPs
        packet.position(12)
        val sourceIp = ByteArray(4)
        packet.get(sourceIp)
        val destIp = ByteArray(4)
        packet.get(destIp)

        // Position buffer at the start of the payload (e.g., TCP header)
        packet.position(headerLength)

        return IpHeader(
            version = version,
            headerLength = headerLength,
            protocol = protocol,
            sourceIp = sourceIp.joinToString(".") { (it.toInt() and 0xFF).toString() },
            destinationIp = destIp.joinToString(".") { (it.toInt() and 0xFF).toString() }
        )
    }

    fun parseTcpHeader(packet: ByteBuffer): TcpHeader {
        val sourcePort = packet.getShort().toInt() and 0xFFFF
        val destPort = packet.getShort().toInt() and 0xFFFF

        // Skip sequence and ack numbers
        packet.position(packet.position() + 8)

        // Find SYN flag
        packet.position(packet.position() + 1)
        val flags = packet.get().toInt() and 0xFF
        val isSyn = (flags and 0x02) != 0

        return TcpHeader(sourcePort, destPort, isSyn)
    }

    fun parseUdpHeader(packet: ByteBuffer): UdpHeader {
        val sourcePort = packet.getShort().toInt() and 0xFFFF
        val destinationPort = packet.getShort().toInt() and 0xFFFF
        return UdpHeader(sourcePort, destinationPort)
    }
}