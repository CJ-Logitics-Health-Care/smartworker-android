package com.devjsg.cj_logistics_future_technology.presentation.viewmodel

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.lifecycle.ViewModel
import com.devjsg.cj_logistics_future_technology.data.source.remote.HeartRateService
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class HeartRateViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _heartRate = MutableStateFlow(0)
    val heartRate: StateFlow<Int> = _heartRate

    private val _heartRateAvg = MutableStateFlow(0)
    val heartRateAvg: StateFlow<Int> = _heartRateAvg

    private val heartRateReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.let {
                if (it.action == "com.devjsg.watch.HEART_RATE_UPDATE") {
                    val heartRate = it.getIntExtra("heartRate", 0)
                    _heartRate.value = heartRate
                }
                if (it.action == "com.devjsg.watch.HEART_RATE_AVG_UPDATE") {
                    val heartRateAvg = it.getIntExtra("heartRateAvg", 0)
                    _heartRateAvg.value = heartRateAvg
                }
            }
        }
    }

    init {
        val intentFilter = IntentFilter().apply {
            addAction("com.devjsg.watch.HEART_RATE_UPDATE")
            addAction("com.devjsg.watch.HEART_RATE_AVG_UPDATE")
        }
        context.registerReceiver(heartRateReceiver, intentFilter, Context.RECEIVER_NOT_EXPORTED)
    }

    override fun onCleared() {
        super.onCleared()
        context.unregisterReceiver(heartRateReceiver)
    }

    fun startService() {
        HeartRateService.startService(context)
    }

    fun stopService() {
        HeartRateService.stopService(context)
    }
}