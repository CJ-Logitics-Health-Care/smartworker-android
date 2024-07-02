package com.devjsg.watch.presentation.viewmodel

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devjsg.watch.data.DataSyncManager
import com.devjsg.watch.data.HeartRateRepository
import com.devjsg.watch.data.MeasureMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HeartRateViewModel @Inject constructor(
    private val heartRateRepository: HeartRateRepository,
    private val dataSyncManager: DataSyncManager
) : ViewModel() {
    private val _heartRate = MutableStateFlow(0)
    val heartRate: StateFlow<Int> = _heartRate

    private var monitoringJob: Job? = null

    @OptIn(ExperimentalCoroutinesApi::class)
    fun startMonitoring() {
        monitoringJob?.cancel()
        monitoringJob = viewModelScope.launch {
            heartRateRepository.heartRateMeasureFlow()
                .collect { message ->
                    when (message) {
                        is MeasureMessage.MeasureData -> {
                            val heartRateValue = message.data.first().value.toInt()
                            Log.d(TAG, "💓 Updating heart rate value: $heartRateValue")
                            _heartRate.value = heartRateValue
                            dataSyncManager.sendDataToPhone(heartRateValue)
                        }
                        is MeasureMessage.MeasureAvailability -> {
                            // Handle availability change if needed
                            Log.d(TAG, "📶 Availability changed: ${message.availability}")
                        }
                    }
                }
        }
    }

    fun stopMonitoring() {
        monitoringJob?.cancel()
    }
}