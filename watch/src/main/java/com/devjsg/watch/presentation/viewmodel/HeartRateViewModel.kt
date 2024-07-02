package com.devjsg.watch.presentation.viewmodel

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.lifecycle.AndroidViewModel
import com.devjsg.watch.presentation.home.HeartRateService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class HeartRateViewModel @Inject constructor(
    application: Application
) : AndroidViewModel(application) {
    private val _heartRate = MutableStateFlow(0)
    val heartRate: StateFlow<Int> = _heartRate

    private val heartRateReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val heartRate = intent?.getIntExtra("heartRate", 0) ?: 0
            _heartRate.value = heartRate
        }
    }

    init {
        val intentFilter = IntentFilter("com.devjsg.watch.HEART_RATE_UPDATE")
        application.registerReceiver(heartRateReceiver, intentFilter)
    }

    override fun onCleared() {
        super.onCleared()
        getApplication<Application>().unregisterReceiver(heartRateReceiver)
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