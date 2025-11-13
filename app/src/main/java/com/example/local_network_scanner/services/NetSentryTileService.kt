package com.example.local_network_scanner.services

import android.content.Intent
import android.graphics.drawable.Icon
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import com.example.local_network_scanner.R
import com.example.local_network_scanner.data.datastore.VpnStateRepository
import com.example.local_network_scanner.vpn.NetSentryVpnService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class NetSentryTileService : TileService() {

    @Inject
    lateinit var vpnStateRepository: VpnStateRepository

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    override fun onStartListening() {
        super.onStartListening()
        scope.launch {
            vpnStateRepository.vpnState.collectLatest { isActive ->
                updateTile(isActive)
            }
        }
    }

    override fun onClick() {
        super.onClick()
        val intent = if (qsTile.state == Tile.STATE_INACTIVE) {
            Intent(this, NetSentryVpnService::class.java).setAction(NetSentryVpnService.ACTION_START_VPN)
        } else {
            Intent(this, NetSentryVpnService::class.java).setAction(NetSentryVpnService.ACTION_STOP_VPN)
        }
        startService(intent)
    }

    private fun updateTile(isActive: Boolean) {
        qsTile.state = if (isActive) Tile.STATE_ACTIVE else Tile.STATE_INACTIVE
        qsTile.icon = Icon.createWithResource(this, if (isActive) R.drawable.ic_qs_shield else R.drawable.ic_qs_power)
        qsTile.label = if (isActive) "NetSentry (Active)" else "NetSentry (Inactive)"
        qsTile.updateTile()
    }
}