package com.example.local_network_scanner.proxy

import android.net.VpnService
import com.example.local_network_scanner.data.db.LogDao
import com.example.local_network_scanner.data.db.LogEntry
import com.example.local_network_scanner.vpn.PacketParser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.SocketChannel

class TcpProxy(
    private val vpnService: VpnService,
    private val logDao: LogDao
) {

    private val scope = CoroutineScope(Dispatchers.IO)

    fun proxy(packageName: String, appName: String, ipHeader: PacketParser.IpHeader, tcpHeader: PacketParser.TcpHeader, vpnInput: ByteBuffer, vpnOutput: (ByteBuffer) -> Unit) {
        val destinationAddress = InetSocketAddress(ipHeader.destinationIp, tcpHeader.destinationPort)

        try {
            val remoteSocket = SocketChannel.open()
            vpnService.protect(remoteSocket.socket())
            remoteSocket.connect(destinationAddress)

            // Log every successful TCP connection
            val isUnencrypted = tcpHeader.destinationPort == 80
            scope.launch {
                logDao.insert(
                    LogEntry(
                        timestamp = System.currentTimeMillis(),
                        appName = appName,
                        packageName = packageName,
                        destinationIp = ipHeader.destinationIp,
                        destinationPort = tcpHeader.destinationPort,
                        protocol = "TCP",
                        status = "ALLOWED",
                        isUnencrypted = isUnencrypted
                    )
                )
            }

            // Start proxying data
            scope.launch {
                try {
                    val buffer = ByteBuffer.allocate(32767)
                    while (remoteSocket.read(buffer) != -1) {
                        buffer.flip()
                        vpnOutput(buffer)
                        buffer.clear()
                    }
                } catch (_: IOException) {
                    // Connection closed
                }
            }

            scope.launch {
                try {
                    while (vpnInput.hasRemaining()) {
                        remoteSocket.write(vpnInput)
                    }
                } catch (_: IOException) {
                    // Connection closed
                }
            }
        } catch (_: IOException) {
            // Failed to connect, do nothing.
        }
    }
}
