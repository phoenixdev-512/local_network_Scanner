package com.example.local_network_scanner.data.repository

import android.util.Log
import com.example.local_network_scanner.data.db.UserProfile
import com.example.local_network_scanner.data.db.UserProfileDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository for UserProfile data operations with error handling
 */
@Singleton
class ProfileRepository @Inject constructor(
    private val profileDao: UserProfileDao
) {
    val allProfiles: Flow<List<UserProfile>> = profileDao.getAllProfiles()
    val activeProfile: Flow<UserProfile?> = profileDao.getActiveProfile()
    
    suspend fun createProfile(profile: UserProfile): Result<Long> {
        return try {
            val id = profileDao.insertProfile(profile)
            Result.success(id)
        } catch (e: Exception) {
            Log.e("ProfileRepository", "Error creating profile", e)
            Result.failure(e)
        }
    }
    
    suspend fun updateProfile(profile: UserProfile): Result<Unit> {
        return try {
            profileDao.updateProfile(profile)
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("ProfileRepository", "Error updating profile", e)
            Result.failure(e)
        }
    }
    
    suspend fun deleteProfile(profile: UserProfile): Result<Unit> {
        return try {
            if (profile.isActive) {
                return Result.failure(Exception("Cannot delete active profile"))
            }
            profileDao.deleteProfile(profile)
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("ProfileRepository", "Error deleting profile", e)
            Result.failure(e)
        }
    }
    
    suspend fun switchProfile(profileId: Long): Result<Unit> {
        return try {
            profileDao.deactivateAllProfiles()
            profileDao.activateProfile(profileId)
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("ProfileRepository", "Error switching profile", e)
            Result.failure(e)
        }
    }
    
    suspend fun getProfileById(id: Long): UserProfile? {
        return profileDao.getProfileById(id)
    }
    
    suspend fun updateLastActive(profileId: Long) {
        try {
            profileDao.updateLastActive(profileId)
        } catch (e: Exception) {
            Log.e("ProfileRepository", "Error updating last active", e)
        }
    }
}
