package com.example.local_network_scanner.ui.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.local_network_scanner.data.db.UserProfile
import com.example.local_network_scanner.data.db.UserRole
import com.example.local_network_scanner.data.repository.ProfileRepository
import com.example.local_network_scanner.util.ImageStorageService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for Profile Management
 * Manages user profile data, CRUD operations, and UI state
 */
@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: ProfileRepository,
    private val imageStorage: ImageStorageService
) : ViewModel() {
    
    // Current active profile
    private val _currentProfile = MutableStateFlow<UserProfile?>(null)
    val currentProfile: StateFlow<UserProfile?> = _currentProfile.asStateFlow()
    
    // All user profiles
    val profiles = repository.allProfiles.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )
    
    // Active profile from repository
    val activeProfile = repository.activeProfile.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        null
    )
    
    // UI state for operations
    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Idle)
    val uiState: StateFlow<ProfileUiState> = _uiState
    
    // Delete dialog state
    private val _showDeleteDialog = MutableStateFlow(false)
    val showDeleteDialog: StateFlow<Boolean> = _showDeleteDialog
    
    private val _profileToDelete = MutableStateFlow<UserProfile?>(null)
    val profileToDelete: StateFlow<UserProfile?> = _profileToDelete
    
    init {
        // Observe active profile changes
        viewModelScope.launch {
            activeProfile.collect { profile ->
                _currentProfile.value = profile
            }
        }
    }
    
    /**
     * Create a new user profile
     */
    fun createProfile(
        name: String,
        email: String,
        role: UserRole,
        avatarUri: Uri?,
        autoStartVpn: Boolean,
        notificationsEnabled: Boolean,
        customDnsServer: String?
    ) {
        viewModelScope.launch {
            _uiState.value = ProfileUiState.Loading
            
            // Validate inputs
            if (name.isBlank()) {
                _uiState.value = ProfileUiState.Error("Name is required")
                return@launch
            }
            
            if (!isValidEmail(email)) {
                _uiState.value = ProfileUiState.Error("Invalid email format")
                return@launch
            }
            
            try {
                val savedAvatarPath = avatarUri?.let { uri ->
                    imageStorage.saveImage(uri, "profile_${System.currentTimeMillis()}.jpg")
                }
                
                val profile = UserProfile(
                    name = name.trim(),
                    email = email.trim().lowercase(),
                    role = role,
                    avatarUri = savedAvatarPath,
                    autoStartVpn = autoStartVpn,
                    notificationsEnabled = notificationsEnabled,
                    customDnsServer = customDnsServer?.takeIf { it.isNotBlank() }
                )
                
                val result = repository.createProfile(profile)
                result.fold(
                    onSuccess = { id ->
                        _uiState.value = ProfileUiState.Success("Profile created successfully")
                    },
                    onFailure = { error ->
                        _uiState.value = ProfileUiState.Error("Failed to create profile: ${error.message}")
                    }
                )
            } catch (e: Exception) {
                _uiState.value = ProfileUiState.Error("Unexpected error: ${e.message}")
            }
        }
    }
    
    /**
     * Update an existing user profile
     */
    fun updateProfile(
        profileId: Long,
        name: String,
        email: String,
        role: UserRole,
        avatarUri: Uri?,
        autoStartVpn: Boolean,
        notificationsEnabled: Boolean,
        customDnsServer: String?
    ) {
        viewModelScope.launch {
            _uiState.value = ProfileUiState.Loading
            
            // Validate inputs
            if (name.isBlank()) {
                _uiState.value = ProfileUiState.Error("Name is required")
                return@launch
            }
            
            if (!isValidEmail(email)) {
                _uiState.value = ProfileUiState.Error("Invalid email format")
                return@launch
            }
            
            try {
                val existingProfile = repository.getProfileById(profileId)
                if (existingProfile == null) {
                    _uiState.value = ProfileUiState.Error("Profile not found")
                    return@launch
                }
                
                val updatedAvatarPath = if (avatarUri != null) {
                    imageStorage.saveImage(avatarUri, "profile_${profileId}.jpg")
                } else {
                    existingProfile.avatarUri
                }
                
                val updatedProfile = existingProfile.copy(
                    name = name.trim(),
                    email = email.trim().lowercase(),
                    role = role,
                    avatarUri = updatedAvatarPath,
                    autoStartVpn = autoStartVpn,
                    notificationsEnabled = notificationsEnabled,
                    customDnsServer = customDnsServer?.takeIf { it.isNotBlank() }
                )
                
                val result = repository.updateProfile(updatedProfile)
                result.fold(
                    onSuccess = {
                        _uiState.value = ProfileUiState.Success("Profile updated successfully")
                    },
                    onFailure = { error ->
                        _uiState.value = ProfileUiState.Error("Failed to update profile: ${error.message}")
                    }
                )
            } catch (e: Exception) {
                _uiState.value = ProfileUiState.Error("Unexpected error: ${e.message}")
            }
        }
    }
    
    /**
     * Request to delete a profile (shows confirmation dialog)
     */
    fun requestDeleteProfile(profile: UserProfile) {
        _profileToDelete.value = profile
        _showDeleteDialog.value = true
    }
    
    /**
     * Confirm and execute profile deletion
     */
    fun confirmDeleteProfile() {
        viewModelScope.launch {
            val profile = _profileToDelete.value ?: return@launch
            _uiState.value = ProfileUiState.Loading
            _showDeleteDialog.value = false
            
            val result = repository.deleteProfile(profile)
            result.fold(
                onSuccess = {
                    // Delete avatar if exists
                    profile.avatarUri?.let { path ->
                        imageStorage.deleteImage(path)
                    }
                    _uiState.value = ProfileUiState.Success("Profile deleted successfully")
                    _profileToDelete.value = null
                },
                onFailure = { error ->
                    _uiState.value = ProfileUiState.Error("Failed to delete profile: ${error.message}")
                }
            )
        }
    }
    
    /**
     * Cancel profile deletion
     */
    fun cancelDeleteProfile() {
        _showDeleteDialog.value = false
        _profileToDelete.value = null
    }
    
    /**
     * Switch to a different profile
     */
    fun switchProfile(profileId: Long) {
        viewModelScope.launch {
            _uiState.value = ProfileUiState.Loading
            
            val result = repository.switchProfile(profileId)
            result.fold(
                onSuccess = {
                    _uiState.value = ProfileUiState.Success("Profile switched successfully")
                },
                onFailure = { error ->
                    _uiState.value = ProfileUiState.Error("Failed to switch profile: ${error.message}")
                }
            )
        }
    }
    
    /**
     * Reset UI state to idle
     */
    fun resetUiState() {
        _uiState.value = ProfileUiState.Idle
    }
    
    /**
     * Validate email format using regex
     */
    private fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()
        return email.matches(emailRegex)
    }
}

/**
 * UI State for profile operations
 */
sealed class ProfileUiState {
    object Idle : ProfileUiState()
    object Loading : ProfileUiState()
    data class Success(val message: String) : ProfileUiState()
    data class Error(val message: String) : ProfileUiState()
}

