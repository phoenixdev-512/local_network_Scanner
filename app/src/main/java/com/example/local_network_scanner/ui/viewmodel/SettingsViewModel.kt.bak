package com.example.local_network_scanner.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.local_network_scanner.data.datastore.DnsSettings
import com.example.local_network_scanner.data.datastore.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    val blockAllByDefault = settingsRepository.blockAllByDefault()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val dnsSettings = settingsRepository.getDnsSettings()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), DnsSettings())

    val enableWeeklySummary = settingsRepository.getEnableWeeklySummary()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val adBlockingEnabled = settingsRepository.getAdBlockingEnabled()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val malwareBlockingEnabled = settingsRepository.getMalwareBlockingEnabled()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val notifyOnThreats = settingsRepository.getNotifyOnThreats()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val notifyOnNewApps = settingsRepository.getNotifyOnNewApps()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val blocklistLastUpdated = settingsRepository.getBlocklistLastUpdated()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "Never")

    fun setBlockAllByDefault(block: Boolean) {
        viewModelScope.launch {
            settingsRepository.setBlockAllByDefault(block)
        }
    }

    fun setDnsMode(mode: String) {
        viewModelScope.launch {
            settingsRepository.setDnsMode(mode)
        }
    }

    fun setCustomDnsIp(ip: String) {
        viewModelScope.launch {
            settingsRepository.setCustomDnsIp(ip)
        }
    }

    fun setEnableSecureDns(enable: Boolean) {
        viewModelScope.launch {
            settingsRepository.setEnableSecureDns(enable)
        }
    }

    fun setEnableWeeklySummary(enable: Boolean) {
        viewModelScope.launch {
            settingsRepository.setEnableWeeklySummary(enable)
        }
    }

    fun setAdBlockingEnabled(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.setAdBlockingEnabled(enabled)
        }
    }

    fun setMalwareBlockingEnabled(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.setMalwareBlockingEnabled(enabled)
        }
    }

    fun setNotifyOnThreats(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.setNotifyOnThreats(enabled)
        }
    }

    fun setNotifyOnNewApps(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.setNotifyOnNewApps(enabled)
        }
    }
    
    // TODO: Add function to trigger blocklist update
}