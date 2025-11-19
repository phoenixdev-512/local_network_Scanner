package com.example.local_network_scanner.util

import android.app.Activity
import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.provider.Settings

/**
 * Helper functions for requesting and checking usage stats permissions
 */
object PermissionHelper {
    
    /**
     * Request usage stats permission by opening the settings page
     */
    fun requestUsageStatsPermission(activity: Activity) {
        if (!hasUsageStatsPermission(activity)) {
            val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
            activity.startActivity(intent)
        }
    }
    
    /**
     * Check if the app has usage stats permission
     */
    fun hasUsageStatsPermission(context: Context): Boolean {
        return try {
            val appOps = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
            val mode = appOps.checkOpNoThrow(
                AppOpsManager.OPSTR_GET_USAGE_STATS,
                android.os.Process.myUid(),
                context.packageName
            )
            mode == AppOpsManager.MODE_ALLOWED
        } catch (e: Exception) {
            false
        }
    }
}
