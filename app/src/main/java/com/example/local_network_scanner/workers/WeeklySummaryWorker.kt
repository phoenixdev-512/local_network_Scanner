package com.example.local_network_scanner.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.local_network_scanner.data.db.DailyStatsDao
import com.example.local_network_scanner.util.NotificationHelper
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first
import java.time.LocalDate

@HiltWorker
class WeeklySummaryWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val statsDao: DailyStatsDao,
    private val notificationHelper: NotificationHelper
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val sevenDaysAgo = LocalDate.now().minusDays(7).toString()
        val stats = statsDao.getStatsSince(sevenDaysAgo).first()
        val totalBlocked = stats.sumOf { it.connectionsBlocked }

        val summary = "This week, SENET blocked $totalBlocked connections."
        notificationHelper.showSummaryNotification("Weekly Report", summary)

        return Result.success()
    }
}