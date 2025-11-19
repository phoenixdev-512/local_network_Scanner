package com.example.local_network_scanner.ui.viewmodel

import android.net.wifi.WifiManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.local_network_scanner.data.db.NetworkPolicy
import com.example.local_network_scanner.data.db.SavedNetwork
import com.example.local_network_scanner.data.repository.AnalyticsRepository
import com.example.local_network_scanner.data.repository.NetworkRepository
import com.example.local_network_scanner.data.repository.PolicyRepository
import com.example.local_network_scanner.data.repository.TimeRange
import com.example.local_network_scanner.data.repository.WifiNetwork
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for Network Manager screen
 * Manages saved networks, policies, and analytics
 */
@HiltViewModel
class NetworkManagerViewModel @Inject constructor(
    private val networkRepository: NetworkRepository,
    private val policyRepository: PolicyRepository,
    private val analyticsRepository: AnalyticsRepository,
    private val wifiManager: WifiManager
) : ViewModel() {
    
    // Saved Networks state
    val savedNetworks = networkRepository.allNetworks
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    
    // Available networks from scan
    private val _availableNetworks = MutableStateFlow<List<WifiNetwork>>(emptyList())
    val availableNetworks: StateFlow<List<WifiNetwork>> = _availableNetworks
    
    // Scanning state
    private val _isScanning = MutableStateFlow(false)
    val isScanning: StateFlow<Boolean> = _isScanning
    
    // Policies state
    val policies = policyRepository.allPolicies
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    
    // Analytics state
    private val _selectedTimeRange = MutableStateFlow(TimeRange.HOURS_24)
    val selectedTimeRange: StateFlow<TimeRange> = _selectedTimeRange
    
    private val _currentSsid = MutableStateFlow<String?>(null)
    
    val analyticsData = combine(_selectedTimeRange, _currentSsid) { range, ssid ->
        ssid?.let { analyticsRepository.getAnalytics(it, range) } ?: flowOf(emptyList())
    }.flatMapLatest { it }.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
    )
    
    // Current network info
    private val _currentNetwork = MutableStateFlow<CurrentNetworkInfo?>(null)
    val currentNetwork: StateFlow<CurrentNetworkInfo?> = _currentNetwork
    
    // Dialog states
    private val _showNetworkDialog = MutableStateFlow(false)
    val showNetworkDialog: StateFlow<Boolean> = _showNetworkDialog
    
    private val _networkToEdit = MutableStateFlow<SavedNetwork?>(null)
    val networkToEdit: StateFlow<SavedNetwork?> = _networkToEdit
    
    private val _showPolicyDialog = MutableStateFlow(false)
    val showPolicyDialog: StateFlow<Boolean> = _showPolicyDialog
    
    private val _policyToEdit = MutableStateFlow<NetworkPolicy?>(null)
    val policyToEdit: StateFlow<NetworkPolicy?> = _policyToEdit
    
    // UI state
    private val _uiState = MutableStateFlow<NetworkManagerUiState>(NetworkManagerUiState.Idle)
    val uiState: StateFlow<NetworkManagerUiState> = _uiState
    
    init {
        monitorCurrentNetwork()
    }
    
    // Network operations
    fun scanNetworks() {
        viewModelScope.launch {
            _isScanning.value = true
            try {
                val networks = networkRepository.scanNetworks()
                _availableNetworks.value = networks
            } catch (e: Exception) {
                _uiState.value = NetworkManagerUiState.Error("Failed to scan: ${e.message}")
            } finally {
                _isScanning.value = false
            }
        }
    }
    
    fun saveNetwork(network: SavedNetwork) {
        viewModelScope.launch {
            _uiState.value = NetworkManagerUiState.Loading
            val result = networkRepository.saveNetwork(network)
            _uiState.value = result.fold(
                onSuccess = { NetworkManagerUiState.Success("Network saved") },
                onFailure = { NetworkManagerUiState.Error(it.message ?: "Failed to save") }
            )
        }
    }
    
    fun forgetNetwork(ssid: String) {
        viewModelScope.launch {
            _uiState.value = NetworkManagerUiState.Loading
            val result = networkRepository.deleteNetwork(ssid)
            _uiState.value = result.fold(
                onSuccess = { NetworkManagerUiState.Success("Network forgotten") },
                onFailure = { NetworkManagerUiState.Error(it.message ?: "Failed to delete") }
            )
        }
    }
    
    fun connectToNetwork(ssid: String) {
        viewModelScope.launch {
            _uiState.value = NetworkManagerUiState.Loading
            val result = networkRepository.connectToNetwork(ssid)
            _uiState.value = result.fold(
                onSuccess = { connected ->
                    if (connected) NetworkManagerUiState.Success("Connected to $ssid")
                    else NetworkManagerUiState.Error("Failed to connect")
                },
                onFailure = { NetworkManagerUiState.Error(it.message ?: "Connection failed") }
            )
        }
    }
    
    fun showEditDialog(network: SavedNetwork) {
        _networkToEdit.value = network
        _showNetworkDialog.value = true
    }
    
    fun hideNetworkDialog() {
        _showNetworkDialog.value = false
        _networkToEdit.value = null
    }
    
    // Policy operations
    fun createPolicy(policy: NetworkPolicy) {
        viewModelScope.launch {
            _uiState.value = NetworkManagerUiState.Loading
            val result = policyRepository.createPolicy(policy)
            _uiState.value = result.fold(
                onSuccess = { NetworkManagerUiState.Success("Policy created") },
                onFailure = { NetworkManagerUiState.Error(it.message ?: "Failed to create") }
            )
        }
    }
    
    fun updatePolicy(policy: NetworkPolicy) {
        viewModelScope.launch {
            _uiState.value = NetworkManagerUiState.Loading
            val result = policyRepository.updatePolicy(policy)
            _uiState.value = result.fold(
                onSuccess = { NetworkManagerUiState.Success("Policy updated") },
                onFailure = { NetworkManagerUiState.Error(it.message ?: "Failed to update") }
            )
        }
    }
    
    fun deletePolicy(policyId: Long) {
        viewModelScope.launch {
            _uiState.value = NetworkManagerUiState.Loading
            val result = policyRepository.deletePolicy(policyId)
            _uiState.value = result.fold(
                onSuccess = { NetworkManagerUiState.Success("Policy deleted") },
                onFailure = { NetworkManagerUiState.Error(it.message ?: "Failed to delete") }
            )
        }
    }
    
    fun updatePolicyStatus(policyId: Long, isActive: Boolean) {
        viewModelScope.launch {
            if (isActive) {
                policyRepository.activatePolicy(policyId)
            }
        }
    }
    
    fun showPolicyDialog(policy: NetworkPolicy? = null) {
        _policyToEdit.value = policy
        _showPolicyDialog.value = true
    }
    
    fun hidePolicyDialog() {
        _showPolicyDialog.value = false
        _policyToEdit.value = null
    }
    
    fun setTimeRange(range: TimeRange) {
        _selectedTimeRange.value = range
    }
    
    private fun monitorCurrentNetwork() {
        viewModelScope.launch {
            while (isActive) {
                // Get current WiFi connection info
                val wifiInfo = wifiManager.connectionInfo
                _currentSsid.value = wifiInfo?.ssid?.removeSurrounding("\"")
                
                // Update current network info (placeholder data)
                _currentSsid.value?.let { ssid ->
                    _currentNetwork.value = CurrentNetworkInfo(
                        ssid = ssid,
                        currentSpeed = 0.0,
                        activeConnections = 0,
                        blockedThreats = 0
                    )
                }
                
                delay(5000) // Update every 5 seconds
            }
        }
    }
    
    fun resetUiState() {
        _uiState.value = NetworkManagerUiState.Idle
    }
}

sealed class NetworkManagerUiState {
    object Idle : NetworkManagerUiState()
    object Loading : NetworkManagerUiState()
    data class Success(val message: String) : NetworkManagerUiState()
    data class Error(val message: String) : NetworkManagerUiState()
}

data class CurrentNetworkInfo(
    val ssid: String,
    val currentSpeed: Double,
    val activeConnections: Int,
    val blockedThreats: Int
)
