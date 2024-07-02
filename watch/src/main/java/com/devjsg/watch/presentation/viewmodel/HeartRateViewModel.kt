package com.devjsg.watch.presentation.viewmodel

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.devjsg.watch.presentation.home.HeartRateService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HeartRateViewModel @Inject constructor(
    application: Application
) : AndroidViewModel(application) {
    private val _heartRate = MutableStateFlow(0)
    val heartRate: StateFlow<Int> = _heartRate

    private val heartRateList = mutableListOf<Int>()

    private val _heartRateAvg = MutableStateFlow(0)
    val heartRateAvg: StateFlow<Int> = _heartRateAvg

    private val heartRateReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val heartRate = intent?.getIntExtra("heartRate", 0) ?: 0
            if (heartRate != 0) {
                heartRateList.add(heartRate)
                _heartRate.value = calculateAverageHeartRate()
            }
        }
    }

    init {
        val intentFilter = IntentFilter("com.devjsg.watch.HEART_RATE_UPDATE")
        application.registerReceiver(heartRateReceiver, intentFilter)
        startHeartRateCollection()
    }

    override fun onCleared() {
        super.onCleared()
        getApplication<Application>().unregisterReceiver(heartRateReceiver)
    }

    private fun startHeartRateCollection() {
        viewModelScope.launch {
            while (isActive) {
                delay(60 * 1000) // 1분 대기
                _heartRateAvg.value = calculateAverageHeartRate()
                heartRateList.clear()
            }
        }
    }

    private fun calculateAverageHeartRate(): Int {
        return if (heartRateList.isNotEmpty()) {
            heartRateList.sum() / heartRateList.size
        } else {
            0
        }
    }

    fun startService(context: Context) {
        HeartRateService.startService(context)
    }

    fun stopService(context: Context) {
        HeartRateService.stopService(context)
    }

    companion object {
        private const val TAG = "HeartRateViewModel"
    }
}