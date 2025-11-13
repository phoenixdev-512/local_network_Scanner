package com.example.local_network_scanner.data.db

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface ProfileDao {

    @Transaction
    @Query("SELECT * FROM profiles WHERE id = :id")
    fun getProfileWithRules(id: Long): Flow<ProfileWithRules>

    @Transaction
    @Query("SELECT * FROM profiles WHERE isActive = 1")
    fun getActiveProfileWithRules(): Flow<ProfileWithRules?>

    @Query("SELECT * FROM profiles WHERE isActive = 1")
    fun getActiveProfile(): Flow<Profile?>

    @Upsert
    suspend fun saveRule(rule: ProfileRule)

    @Query("UPDATE profiles SET isActive = 0")
    suspend fun deactivateAllProfiles()

    @Query("UPDATE profiles SET isActive = 1 WHERE id = :profileId")
    suspend fun setActiveProfile(profileId: Long)

    @Transaction
    suspend fun switchActiveProfile(profileId: Long) {
        deactivateAllProfiles()
        setActiveProfile(profileId)
    }

    @Query("SELECT * FROM profiles")
    fun getAllProfiles(): Flow<List<Profile>>

    @Upsert
    suspend fun saveProfile(profile: Profile): Long
}
