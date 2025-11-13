package com.example.local_network_scanner.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.local_network_scanner.data.db.UserProfile
import com.example.local_network_scanner.data.db.UserRole
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for Profile screen
 * Manages user profile data and preferences
 */
@HiltViewModel
class ProfileViewModel @Inject constructor(
    // TODO: Inject UserProfileRepository when implemented
) : ViewModel() {
    
    private val _currentProfile = MutableStateFlow<UserProfile?>(null)
    val currentProfile: StateFlow<UserProfile?> = _currentProfile.asStateFlow()
    
    private val _allProfiles = MutableStateFlow<List<UserProfile>>(emptyList())
    val allProfiles: StateFlow<List<UserProfile>> = _allProfiles.asStateFlow()
    
    init {
        loadCurrentProfile()
    }
    
    private fun loadCurrentProfile() {
        viewModelScope.launch {
            // TODO: Load from repository
            // For now, create a mock admin profile
            _currentProfile.value = UserProfile(
                id = 1,
                name = "Admin User",
                email = "admin@example.com",
                role = UserRole.ADMIN,
                autoStartVpn = true,
                notificationsEnabled = true,
                darkMode = true,
                selectedTheme = "speedtest"
            )
        }
    }
    
    fun updateProfile(profile: UserProfile) {
        viewModelScope.launch {
            // TODO: Update in repository
            _currentProfile.value = profile
        }
    }
    
    fun switchProfile(profileId: Long) {
        viewModelScope.launch {
            // TODO: Switch active profile
        }
    }
}
