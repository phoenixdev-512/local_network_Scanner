package com.example.local_network_scanner.vpn

import android.app.Service
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.VpnService
import android.os.ParcelFileDescriptor
import android.util.Log
import com.example.local_network_scanner.data.datastore.SettingsRepository
import com.example.local_network_scanner.data.datastore.VpnStateRepository
import com.example.local_network_scanner.data.db.AppUsageDao
import com.example.local_network_scanner.data.db.LogDao
import com.example.local_network_scanner.data.db.ProfileDao
import com.example.local_network_scanner.proxy.DnsProxy
import com.example.local_network_scanner.proxy.TcpProxy
import com.example.local_network_scanner.proxy.UdpProxy
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.io.FileInputStream
import java.io.FileOutputStream
import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.time.LocalDate
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject

@AndroidEntryPoint
class NetSentryVpnService : VpnService() {

    companion object {
        const val ACTION_START_VPN = "com.example.local_network_scanner.START_VPN"
        const val ACTION_STOP_VPN = "com.example.local_network_scanner.STOP_VPN"
    }

    @Inject lateinit var connectivityManager: ConnectivityManager
    @Inject lateinit var settingsRepository: SettingsRepository
    @Inject lateinit var logDao: LogDao
    @Inject lateinit var profileDao: ProfileDao
    @Inject lateinit var appUsageDao: AppUsageDao
    @Inject lateinit var vpnStateRepository: VpnStateRepository
    @Inject lateinit var geoBlockRepository: com.example.local_network_scanner.data.repository.GeoBlockRepository

    private var vpnInterface: ParcelFileDescriptor? = null
    private val vpnJob = Job()
    private val vpnScope = CoroutineScope(Dispatchers.IO + vpnJob)
    private val packetParser = PacketParser()
    private lateinit var tcpProxy: TcpProxy
    private lateinit var udpProxy: UdpProxy
    private lateinit var dnsProxy: DnsProxy

    private val usageData = ConcurrentHashMap<String, Long>()

    private val TAG = "NetSentryVpnService"

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "VPN service starting")

        when (intent?.action) {
            ACTION_START_VPN -> {
                vpnScope.launch { startVpn() }
                return START_STICKY
            }
            ACTION_STOP_VPN -> {
                stopVpn()
                return START_NOT_STICKY
            }
        }

        vpnScope.launch { startVpn() }
        return START_STICKY
    }

    private suspend fun startVpn() {
        val wifiNetwork = getWifiNetwork()
        if (wifiNetwork == null) {
            Log.e(TAG, "No active Wi-Fi network. Stopping service.")
            stopSelf()
            return
        }

        tcpProxy = TcpProxy(this, logDao)
        udpProxy = UdpProxy(this, connectivityManager, settingsRepository, logDao)
        dnsProxy = DnsProxy()

        val builder = Builder()
            .setSession("NetSentry")
            .addAddress("10.0.0.2", 24)
            .addRoute("0.0.0.0", 0)
            .setUnderlyingNetworks(arrayOf(wifiNetwork))

        settingsRepository.getBypassedApps().first().forEach {
            try {
                builder.addDisallowedApplication(it)
            } catch (e: Exception) {
                Log.e(TAG, "Failed to disallow application: $it", e)
            }
        }

        val settings = settingsRepository.getDnsSettings().first()
        when (settings.dnsMode) {
            "CLOUDFLARE" -> {
                builder.addDnsServer("1.1.1.1")
                builder.addDnsServer("1.0.0.1")
            }
            "GOOGLE" -> {
                builder.addDnsServer("8.8.8.8")
                builder.addDnsServer("8.8.4.4")
            }
            "QUAD9" -> builder.addDnsServer("9.9.9.9")
            "CUSTOM" -> builder.addDnsServer(settings.customDnsIp)
        }

        vpnInterface = builder.establish() ?: run {
            Log.e(TAG, "Failed to establish VPN interface.")
            stopSelf()
            return
        }

        Log.d(TAG, "VPN interface established.")
        vpnStateRepository.vpnState.value = true

        vpnScope.launch {
            startPacketHandling(vpnInterface!!)
        }
        vpnScope.launch { startUsageDataFlush() }
    }

    private fun stopVpn() {
        stopSelf()
    }

    private fun startPacketHandling(vpnInterface: ParcelFileDescriptor) {
        val vpnInput = FileInputStream(vpnInterface.fileDescriptor)
        val vpnOutput = FileOutputStream(vpnInterface.fileDescriptor)
        val packet = ByteBuffer.allocate(32767)

        while (vpnJob.isActive) {
            val bytesRead = vpnInput.read(packet.array())
            if (bytesRead > 0) {
                packet.limit(bytesRead)
                vpnScope.launch {
                    handlePacket(packet.duplicate()) { vpnOutput.write(it) }
                }
                packet.clear()
            }
        }
    }

    private suspend fun startUsageDataFlush() {
        while (vpnJob.isActive) {
            delay(10000)
            val today = LocalDate.now().toString()
            usageData.forEach { (packageName, bytes) ->
                vpnScope.launch { appUsageDao.incrementUsage(packageName, today, bytes) }
            }
            usageData.clear()
        }
    }

    private suspend fun handlePacket(packet: ByteBuffer, vpnOutput: (ByteArray) -> Unit) {
        val ipHeader = packetParser.parseIpHeader(packet)
        val uid = if (ipHeader.protocol == 6) {
            val tcpHeader = packetParser.parseTcpHeader(packet)
            connectivityManager.getConnectionOwnerUid(ipHeader.protocol, InetSocketAddress(ipHeader.sourceIp, tcpHeader.sourcePort), InetSocketAddress(ipHeader.destinationIp, tcpHeader.destinationPort))
        } else if (ipHeader.protocol == 17) {
            val udpHeader = packetParser.parseUdpHeader(packet)
            connectivityManager.getConnectionOwnerUid(ipHeader.protocol, InetSocketAddress(ipHeader.sourceIp, udpHeader.sourcePort), InetSocketAddress(ipHeader.destinationIp, udpHeader.destinationPort))
        } else {
            -1
        }
        val packageName = packageManager.getNameForUid(uid) ?: "Unknown"
        usageData[packageName] = (usageData[packageName] ?: 0) + packet.limit()

        // 1. Check geo-blocking first (block at network level)
        val destinationIp = ipHeader.destinationIp
        val isGeoBlocked = geoBlockRepository.isIpBlocked(destinationIp)
        if (isGeoBlocked) {
            // Drop packet - destination country is blocked
            android.util.Log.d(TAG, "Blocked packet to $destinationIp (geo-blocked country)")
            return
        }

        // 2. Check app-based filtering
        val activeProfile = profileDao.getActiveProfileWithRules().first()
        val rules = activeProfile?.rules?.associate { it.packageName to it.isAllowed } ?: emptyMap()

        val isAllowed = rules[packageName] ?: settingsRepository.blockAllByDefault().first().not()
        if (!isAllowed) {
            return
        }

        if (ipHeader.protocol == 6) { // TCP
            val tcpHeader = packetParser.parseTcpHeader(packet)
            if (tcpHeader.isSyn) {
                tcpProxy.proxy(packageName, "", ipHeader, tcpHeader, packet) { vpnOutput(it.array()) }
            }
        } else if (ipHeader.protocol == 17) { // UDP
            val udpHeader = packetParser.parseUdpHeader(packet)
            val packetBytes = ByteArray(packet.remaining())
            packet.get(packetBytes)
            if (udpHeader.destinationPort == 53 && settingsRepository.getDnsSettings().first().enableSecureDns) {
                dnsProxy.proxy(ipHeader, udpHeader, packetBytes, vpnOutput)
            } else {
                udpProxy.proxy(ipHeader, udpHeader, packetBytes, vpnOutput)
            }
        }
    }

    private fun getWifiNetwork(): Network? {
        return connectivityManager.allNetworks.find { network ->
            val caps = connectivityManager.getNetworkCapabilities(network)
            caps?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "VPN service destroyed")
        vpnStateRepository.vpnState.value = false
        vpnJob.cancel()
        vpnInterface?.close()
    }
}
