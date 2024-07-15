package com.devjsg.cj_logistics_future_technology.data.repository

import android.content.Context
import android.util.Log
import com.devjsg.cj_logistics_future_technology.data.network.HeartRateApiService
import com.devjsg.cj_logistics_future_technology.data.work.HeartRateWorker
import javax.inject.Inject

class HeartRateRepository @Inject constructor(
    private val context: Context,
    private val heartRateApiService: HeartRateApiService
) {
    fun handleReceivedHeartRateAvg(heartRateAvg: Int) {
        Log.d("HeartRateRepository", "Received heart rate avg: $heartRateAvg")
        HeartRateWorker.enqueueWork(context, heartRateAvg)
    }
}