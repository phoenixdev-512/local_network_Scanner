package com.example.local_network_scanner.ui.viewmodel

import android.Manifest
import android.content.Context
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import android.net.wifi.WifiNetworkSpecifier
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.local_network_scanner.receivers.WifiScanReceiver
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WifiViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val _currentSsid = MutableStateFlow("Not Connected")
    val currentSsid = _currentSsid.asStateFlow()

    private val _signalStrength = MutableStateFlow(0)
    val signalStrength = _signalStrength.asStateFlow()

    private val _localIp = MutableStateFlow("")
    val localIp = _localIp.asStateFlow()

    private val _scanResults = MutableStateFlow<List<ScanResult>>(emptyList())
    val scanResults = _scanResults.asStateFlow()

    private val _permissionGranted = MutableStateFlow(false)
    val permissionGranted = _permissionGranted.asStateFlow()

    private val _isScanning = MutableStateFlow(false)
    val isScanning = _isScanning.asStateFlow()

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            updateConnectionInfo()
        }

        override fun onLost(network: Network) {
            _currentSsid.value = "Not Connected"
            _signalStrength.value = 0
            _localIp.value = ""
        }
    }

    private val wifiScanReceiver = WifiScanReceiver { success, results ->
        if (success) {
            _scanResults.value = results
        }
        _isScanning.value = false
    }

    init {
        _permissionGranted.value = ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED

        val networkRequest = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .build()
        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)

        context.registerReceiver(
            wifiScanReceiver,
            IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)
        )

        updateConnectionInfo()
    }

    fun startScan() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            _permissionGranted.value = true
            _isScanning.value = true
            wifiManager.startScan()
        } else {
            _permissionGranted.value = false
        }
    }

    fun connectToNetwork(scanResult: ScanResult, password: String? = null) {
        val specifier = WifiNetworkSpecifier.Builder()
            .setSsid(scanResult.SSID)
            .apply {
                if (password != null) {
                    setWpa2Passphrase(password)
                }
            }
            .build()

        val request = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .setNetworkSpecifier(specifier)
            .build()

        connectivityManager.requestNetwork(request, object : ConnectivityManager.NetworkCallback() {})
    }

    private fun updateConnectionInfo() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            _permissionGranted.value = false
            return
        }
        _permissionGranted.value = true
        val info = wifiManager.connectionInfo
        _currentSsid.value = info.ssid.removeSurrounding("\"")
        _signalStrength.value = info.rssi
        val network = connectivityManager.activeNetwork
        val linkProperties = connectivityManager.getLinkProperties(network)
        _localIp.value = linkProperties?.linkAddresses?.find { it.address is java.net.Inet4Address }?.address?.hostAddress ?: ""
    }

    override fun onCleared() {
        super.onCleared()
        connectivityManager.unregisterNetworkCallback(networkCallback)
        context.unregisterReceiver(wifiScanReceiver)
    }
}
