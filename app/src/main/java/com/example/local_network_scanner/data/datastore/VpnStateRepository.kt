package com.example.local_network_scanner.data.datastore

import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * A singleton repository to hold the global state of the VPN service.
 */
@Singleton
class VpnStateRepository @Inject constructor() {
    val vpnState = MutableStateFlow(false)
}