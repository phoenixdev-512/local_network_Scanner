package com.example.local_network_scanner.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.local_network_scanner.data.datastore.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class GeoBlockViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    val blockedCountries = settingsRepository.getBlockedCountries()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptySet())

    val allCountries = Locale.getISOCountries().map {
        val locale = Locale("", it)
        Pair(it, locale.displayCountry)
    }.sortedBy { it.second }

    fun toggleCountry(isoCode: String) {
        viewModelScope.launch {
            settingsRepository.toggleBlockedCountry(isoCode)
        }
    }
}
