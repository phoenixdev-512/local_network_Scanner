package com.example.local_network_scanner.ui.viewmodel

import android.content.Context
import android.content.pm.PackageManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.local_network_scanner.data.datastore.SettingsRepository
import com.example.local_network_scanner.data.db.AppUsageDao
import com.example.local_network_scanner.data.model.AppInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class AppListViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val settingsRepository: SettingsRepository,
    private val appUsageDao: AppUsageDao
) : ViewModel() {

    private val _apps = MutableStateFlow<List<AppInfo>>(emptyList())
    val apps = _apps.asStateFlow()

    init {
        loadInstalledApps()
    }

    private fun loadInstalledApps() {
        viewModelScope.launch {
            val pm = context.packageManager
            val installedApps = pm.getInstalledApplications(PackageManager.GET_META_DATA)
                .filter { pm.getLaunchIntentForPackage(it.packageName) != null } // Filter for launchable apps

            val today = LocalDate.now().toString()

            combine(
                settingsRepository.getBypassedApps(),
                settingsRepository.blockAllByDefault(),
                appUsageDao.getUsageForDay(today)
            ) { bypassedApps, blockAllByDefault, usageToday ->
                installedApps.map { appInfo ->
                    val appName = appInfo.loadLabel(pm).toString()
                    val packageName = appInfo.packageName
                    val icon = appInfo.loadIcon(pm)
                    val isBypassed = bypassedApps.contains(packageName)
                    val isAllowed = settingsRepository.getRule(packageName).first()
                    val usage = usageToday.find { it.packageName == packageName }?.dataUsedBytes ?: 0

                    AppInfo(appName, packageName, icon, !isAllowed, isBypassed, usage)
                }.sortedBy { it.appName }
            }.collect {
                _apps.value = it
            }
        }
    }

    fun setRule(packageName: String, isBlocked: Boolean) {
        viewModelScope.launch {
            settingsRepository.setRule(packageName, !isBlocked)
        }
    }

    fun toggleBypass(packageName: String) {
        viewModelScope.launch {
            settingsRepository.toggleBypassApp(packageName)
        }
    }
}
