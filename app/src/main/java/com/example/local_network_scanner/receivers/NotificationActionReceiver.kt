package com.example.local_network_scanner.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.local_network_scanner.data.db.ProfileDao
import com.example.local_network_scanner.data.db.ProfileRule
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class NotificationActionReceiver : BroadcastReceiver() {

    @Inject
    lateinit var profileDao: ProfileDao

    private val scope = CoroutineScope(Dispatchers.IO)

    override fun onReceive(context: Context, intent: Intent) {
        val packageName = intent.getStringExtra("packageName") ?: return
        val action = intent.action

        scope.launch {
            val activeProfile = profileDao.getActiveProfile().first() ?: return@launch
            val isAllowed = action == "ALLOW"
            profileDao.saveRule(ProfileRule(profileId = activeProfile.id, packageName = packageName, isAllowed = isAllowed))
        }
    }
}