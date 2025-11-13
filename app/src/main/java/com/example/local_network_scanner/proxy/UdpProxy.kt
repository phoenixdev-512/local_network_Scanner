package com.example.local_network_scanner.proxy

import android.net.ConnectivityManager
import android.net.VpnService
import com.example.local_network_scanner.data.datastore.SettingsRepository
import com.example.local_network_scanner.data.db.LogDao
import com.example.local_network_scanner.data.db.LogEntry
import com.example.local_network_scanner.vpn.PacketParser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.net.InetSocketAddress

class UdpProxy(
    private val vpnService: VpnService,
    private val connectivityManager: ConnectivityManager,
    private val settingsRepository: SettingsRepository,
    private val logDao: LogDao
) {
    private val scope = CoroutineScope(Dispatchers.IO)

    suspend fun proxy(ipHeader: PacketParser.IpHeader, udpHeader: PacketParser.UdpHeader, packet: ByteArray, vpnOutput: (ByteArray) -> Unit) {
        val sourceAddress = InetSocketAddress(ipHeader.sourceIp, udpHeader.sourcePort)
        val destinationAddress = InetSocketAddress(ipHeader.destinationIp, udpHeader.destinationPort)

        val datagramSocket = DatagramSocket()
        vpnService.protect(datagramSocket)

        scope.launch {
            val datagramPacket = DatagramPacket(packet, packet.size, destinationAddress.address, destinationAddress.port)
            datagramSocket.send(datagramPacket)

            val responseBuffer = ByteArray(32767)
            val responsePacket = DatagramPacket(responseBuffer, responseBuffer.size)
            datagramSocket.receive(responsePacket)
            vpnOutput(responsePacket.data)
        }
    }
}