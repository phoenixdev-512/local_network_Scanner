package com.example.local_network_scanner.data.datastore

import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConnectionRepository @Inject constructor() {
    val newConnections = MutableSharedFlow<String>(extraBufferCapacity = 64)
}
