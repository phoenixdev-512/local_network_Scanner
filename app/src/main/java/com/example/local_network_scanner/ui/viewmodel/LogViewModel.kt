package com.example.local_network_scanner.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.local_network_scanner.data.db.LogDao
import com.example.local_network_scanner.data.db.LogEntry
import com.example.local_network_scanner.data.model.ConnectionLog
import com.example.local_network_scanner.ui.LogFilter
import com.example.local_network_scanner.ui.TimeRange
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LogViewModel @Inject constructor(private val logDao: LogDao) : ViewModel() {

    private val _logs = MutableStateFlow<List<LogEntry>>(emptyList())
    val logs = _logs.asStateFlow()
    
    private val _filteredLogs = MutableStateFlow<List<ConnectionLog>>(emptyList())
    val filteredLogs = _filteredLogs.asStateFlow()

    init {
        viewModelScope.launch {
            logDao.getAll().collect {
                _logs.value = it
                // Initialize filtered logs
                applyFilters("", LogFilter.ALL, TimeRange.LAST_HOUR)
            }
        }
    }
    
    fun applyFilters(searchQuery: String, filter: LogFilter, timeRange: TimeRange) {
        val currentTime = System.currentTimeMillis()
        val timeThreshold = currentTime - (timeRange.hours * 60 * 60 * 1000)
        
        var filtered = _logs.value
            .filter { it.timestamp >= timeThreshold }
            .map { it.toConnectionLog() }
        
        // Apply search filter
        if (searchQuery.isNotEmpty()) {
            filtered = filtered.filter {
                it.appName.contains(searchQuery, ignoreCase = true) ||
                it.packageName.contains(searchQuery, ignoreCase = true) ||
                it.destinationIp.contains(searchQuery, ignoreCase = true) ||
                it.destinationPort.toString().contains(searchQuery)
            }
        }
        
        // Apply status filter
        filtered = when (filter) {
            LogFilter.ALL -> filtered
            LogFilter.ALLOWED -> filtered.filter { it.status == "ALLOWED" }
            LogFilter.BLOCKED -> filtered.filter { it.status == "BLOCKED" }
            LogFilter.UNENCRYPTED -> filtered.filter { it.isUnencrypted }
        }
        
        _filteredLogs.value = filtered.sortedByDescending { it.timestamp }
    }
    
    fun refreshLogs() {
        viewModelScope.launch {
            logDao.getAll().collect {
                _logs.value = it
            }
        }
    }

    fun clearLogs() {
        viewModelScope.launch {
            logDao.deleteAll()
        }
    }
    
    private fun LogEntry.toConnectionLog() = ConnectionLog(
        id = id,
        timestamp = timestamp,
        appName = appName,
        packageName = packageName,
        destinationIp = destinationIp,
        destinationPort = destinationPort,
        protocol = protocol,
        status = status,
        isUnencrypted = isUnencrypted
    )
}

