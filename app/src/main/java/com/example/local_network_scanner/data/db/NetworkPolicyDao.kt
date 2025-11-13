package com.example.local_network_scanner.data.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

/**
 * DAO for NetworkPolicy operations
 */
@Dao
interface NetworkPolicyDao {
    @Query("SELECT * FROM network_policies ORDER BY createdAt DESC")
    fun getAllPolicies(): Flow<List<NetworkPolicy>>
    
    @Query("SELECT * FROM network_policies WHERE id = :id")
    suspend fun getPolicyById(id: Long): NetworkPolicy?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPolicy(policy: NetworkPolicy): Long
    
    @Update
    suspend fun updatePolicy(policy: NetworkPolicy)
    
    @Delete
    suspend fun deletePolicy(policy: NetworkPolicy)
    
    @Query("DELETE FROM network_policies WHERE id = :id")
    suspend fun deletePolicyById(id: Long)
}
