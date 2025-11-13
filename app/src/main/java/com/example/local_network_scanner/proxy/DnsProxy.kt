package com.example.local_network_scanner.proxy

import com.example.local_network_scanner.vpn.PacketParser
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class DnsProxy {

    private val client = OkHttpClient()

    fun proxy(ipHeader: PacketParser.IpHeader, udpHeader: PacketParser.UdpHeader, packet: ByteArray, vpnOutput: (ByteArray) -> Unit) {
        val requestBody = packet.toRequestBody()
        val request = Request.Builder()
            .url("https://1.1.1.1/dns-query")
            .post(requestBody)
            .build()

        try {
            val response = client.newCall(request).execute()
            if (response.isSuccessful) {
                val responseBytes = response.body?.bytes()
                if (responseBytes != null) {
                    // A real implementation needs to craft a proper IP/UDP packet.
                    vpnOutput(responseBytes)
                }
            }
        } catch (e: IOException) {
            // DNS query failed
        }
    }
}
