package com.example.local_network_scanner.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.local_network_scanner.data.db.LogDao
import com.example.local_network_scanner.data.db.LogEntry
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LogViewModel @Inject constructor(private val logDao: LogDao) : ViewModel() {

    private val _logs = MutableStateFlow<List<LogEntry>>(emptyList())
    val logs = _logs.asStateFlow()

    init {
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
}
