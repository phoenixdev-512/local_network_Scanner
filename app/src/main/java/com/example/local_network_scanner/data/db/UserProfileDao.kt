package com.example.local_network_scanner.data.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

/**
 * DAO for UserProfile operations with complete CRUD functionality
 */
@Dao
interface UserProfileDao {
    @Query("SELECT * FROM user_profiles ORDER BY lastActiveAt DESC")
    fun getAllProfiles(): Flow<List<UserProfile>>
    
    @Query("SELECT * FROM user_profiles WHERE id = :profileId")
    suspend fun getProfileById(profileId: Long): UserProfile?
    
    @Query("SELECT * FROM user_profiles WHERE isActive = 1 LIMIT 1")
    fun getActiveProfile(): Flow<UserProfile?>
    
    @Query("SELECT * FROM user_profiles WHERE email = :email")
    suspend fun getProfileByEmail(email: String): UserProfile?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profile: UserProfile): Long
    
    @Update
    suspend fun updateProfile(profile: UserProfile)
    
    @Delete
    suspend fun deleteProfile(profile: UserProfile)
    
    @Query("DELETE FROM user_profiles WHERE id = :profileId")
    suspend fun deleteProfileById(profileId: Long)
    
    @Query("UPDATE user_profiles SET isActive = 0")
    suspend fun deactivateAllProfiles()
    
    @Query("UPDATE user_profiles SET isActive = 1, lastActiveAt = :timestamp WHERE id = :profileId")
    suspend fun activateProfile(profileId: Long, timestamp: Long = System.currentTimeMillis())
    
    @Query("UPDATE user_profiles SET lastActiveAt = :timestamp WHERE id = :profileId")
    suspend fun updateLastActive(profileId: Long, timestamp: Long = System.currentTimeMillis())
}
