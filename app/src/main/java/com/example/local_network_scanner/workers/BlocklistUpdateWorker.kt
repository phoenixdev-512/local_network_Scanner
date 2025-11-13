package com.example.local_network_scanner.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.local_network_scanner.data.db.BlocklistDao
import com.example.local_network_scanner.data.db.BlocklistEntry
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

@HiltWorker
class BlocklistUpdateWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val blocklistDao: BlocklistDao
) : CoroutineWorker(appContext, workerParams) {

    private val client = OkHttpClient()

    override suspend fun doWork(): Result {
        return try {
            val hosts = downloadBlocklist("https://raw.githubusercontent.com/StevenBlack/hosts/master/hosts")
            val entries = parseBlocklist(hosts, 1) // 1 for ads
            blocklistDao.insertAll(entries)
            Result.success()
        } catch (e: IOException) {
            Result.failure()
        }
    }

    private fun downloadBlocklist(url: String): String {
        val request = Request.Builder().url(url).build()
        val response = client.newCall(request).execute()
        return response.body?.string() ?: ""
    }

    private fun parseBlocklist(hosts: String, type: Int): List<BlocklistEntry> {
        return hosts.lines()
            .filter { it.startsWith("0.0.0.0") }
            .map { it.split(" ")[1].trim() }
            .map { BlocklistEntry(it, type) }
    }
}
