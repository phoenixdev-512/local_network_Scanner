package com.example.local_network_scanner.data.repository

import com.example.local_network_scanner.data.db.NetworkPolicy
import com.example.local_network_scanner.data.db.NetworkPolicyDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository for network policy operations
 */
@Singleton
class PolicyRepository @Inject constructor(
    private val policyDao: NetworkPolicyDao
) {
    val allPolicies: Flow<List<NetworkPolicy>> = policyDao.getAllPolicies()
    
    suspend fun createPolicy(policy: NetworkPolicy): Result<Long> = withContext(Dispatchers.IO) {
        try {
            val id = policyDao.insertPolicy(policy)
            Result.success(id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun updatePolicy(policy: NetworkPolicy): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            policyDao.updatePolicy(policy)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun deletePolicy(policyId: Long): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            policyDao.deletePolicyById(policyId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun activatePolicy(policyId: Long): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            policyDao.deactivateAllPolicies()
            policyDao.activatePolicy(policyId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getDefaultPolicy(): NetworkPolicy? = withContext(Dispatchers.IO) {
        policyDao.getDefaultPolicy()
    }
}
