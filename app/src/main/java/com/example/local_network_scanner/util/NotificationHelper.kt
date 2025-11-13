package com.example.local_network_scanner.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.local_network_scanner.R
import com.example.local_network_scanner.receivers.NotificationActionReceiver
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationHelper @Inject constructor(@ApplicationContext private val context: Context) {

    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val weeklySummaryChannel = NotificationChannel(
                "weekly_summary",
                "Weekly Summary",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val threatsChannel = NotificationChannel(
                "threats",
                "Threats",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val newAppsChannel = NotificationChannel(
                "new_apps",
                "New Apps",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(weeklySummaryChannel)
            notificationManager.createNotificationChannel(threatsChannel)
            notificationManager.createNotificationChannel(newAppsChannel)
        }
    }

    fun showSummaryNotification(title: String, content: String) {
        val builder = NotificationCompat.Builder(context, "weekly_summary")
            .setSmallIcon(R.drawable.ic_qs_shield)
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        notificationManager.notify(1, builder.build())
    }

    fun showThreatNotification(appName: String, reason: String) {
        val builder = NotificationCompat.Builder(context, "threats")
            .setSmallIcon(R.drawable.ic_qs_shield)
            .setContentTitle("Threat Blocked")
            .setContentText("$appName: $reason")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        notificationManager.notify(2, builder.build())
    }

    fun showNewAppNotification(appName: String, packageName: String) {
        val allowIntent = Intent(context, NotificationActionReceiver::class.java).apply {
            action = "ALLOW"
            putExtra("packageName", packageName)
        }
        val blockIntent = Intent(context, NotificationActionReceiver::class.java).apply {
            action = "BLOCK"
            putExtra("packageName", packageName)
        }

        val allowPendingIntent = PendingIntent.getBroadcast(context, 0, allowIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        val blockPendingIntent = PendingIntent.getBroadcast(context, 1, blockIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        val builder = NotificationCompat.Builder(context, "new_apps")
            .setSmallIcon(R.drawable.ic_qs_shield)
            .setContentTitle("New App Detected")
            .setContentText("$appName is trying to access the network")
            .addAction(0, "Allow", allowPendingIntent)
            .addAction(0, "Block", blockPendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        notificationManager.notify(3, builder.build())
    }
}
