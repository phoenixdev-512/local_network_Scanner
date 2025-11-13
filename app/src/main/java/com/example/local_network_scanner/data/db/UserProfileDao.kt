package com.example.local_network_scanner.data.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

/**
 * DAO for UserProfile operations
 */
@Dao
interface UserProfileDao {
    @Query("SELECT * FROM user_profiles ORDER BY createdAt DESC")
    fun getAllProfiles(): Flow<List<UserProfile>>
    
    @Query("SELECT * FROM user_profiles WHERE id = :id")
    suspend fun getProfileById(id: Long): UserProfile?
    
    @Query("SELECT * FROM user_profiles WHERE email = :email")
    suspend fun getProfileByEmail(email: String): UserProfile?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profile: UserProfile): Long
    
    @Update
    suspend fun updateProfile(profile: UserProfile)
    
    @Delete
    suspend fun deleteProfile(profile: UserProfile)
    
    @Query("DELETE FROM user_profiles WHERE id = :id")
    suspend fun deleteProfileById(id: Long)
}
