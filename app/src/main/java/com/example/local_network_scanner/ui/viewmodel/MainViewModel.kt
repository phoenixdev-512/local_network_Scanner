package com.example.local_network_scanner.ui.viewmodel

import android.content.Context
import android.content.Intent
import android.net.VpnService
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.local_network_scanner.data.db.Profile
import com.example.local_network_scanner.data.db.ProfileDao
import com.example.local_network_scanner.vpn.NetSentryVpnService
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val profileDao: ProfileDao
) : ViewModel() {

    val profiles = profileDao.getAllProfiles()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val activeProfile = profileDao.getActiveProfile()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun startVpn() {
        val intent = VpnService.prepare(context)
        if (intent != null) {
            // In a real app, you would launch this intent and handle the result
        } else {
            val serviceIntent = Intent(context, NetSentryVpnService::class.java)
            context.startService(serviceIntent)
        }
    }

    fun stopVpn() {
        val serviceIntent = Intent(context, NetSentryVpnService::class.java)
        context.stopService(serviceIntent)
    }

    fun setActiveProfile(profile: Profile) {
        viewModelScope.launch {
            profileDao.switchActiveProfile(profile.id)
        }
    }

    fun addProfile(name: String) {
        viewModelScope.launch {
            profileDao.saveProfile(Profile(name = name))
        }
    }
}