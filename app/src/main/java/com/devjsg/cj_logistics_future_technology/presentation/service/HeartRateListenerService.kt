package com.devjsg.cj_logistics_future_technology.presentation.service

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.devjsg.cj_logistics_future_technology.data.repository.HeartRateRepository
import com.devjsg.cj_logistics_future_technology.presentation.viewmodel.WorkerHomeViewModel
import com.google.android.gms.wearable.DataEvent
import com.google.android.gms.wearable.DataEventBuffer
import com.google.android.gms.wearable.DataMapItem
import com.google.android.gms.wearable.WearableListenerService
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HeartRateListenerService : WearableListenerService() {

    @Inject
    lateinit var repository: HeartRateRepository

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "HeartRateListenerService created")
    }

    @SuppressLint("VisibleForTests")
    override fun onDataChanged(dataEvents: DataEventBuffer) {
        Log.d(TAG, "onDataChanged called")
        for (event in dataEvents) {
            Log.d(TAG, "Event type: ${event.type}")
            if (event.type == DataEvent.TYPE_CHANGED) {
                val dataItem = event.dataItem
                Log.d(TAG, "DataItem path: ${dataItem.uri.path}")
                if (dataItem.uri.path == "/heart_rate_avg") {
                    val dataMap = DataMapItem.fromDataItem(dataItem).dataMap
                    val heartRateAvg = dataMap.getInt("heartRateAvg")
                    val timestamp = dataMap.getLong("timestamp")
                    Log.d(TAG, "Received heart rate avg data: $heartRateAvg at $timestamp")
                    handleHeartRateAvgData(heartRateAvg, timestamp)
                }
            }
        }
    }

    private fun handleHeartRateAvgData(heartRateAvg: Int, timestamp: Long) {
        Log.d(TAG, "Handling heart rate avg data: $heartRateAvg at $timestamp")
        repository.handleReceivedHeartRateAvg(heartRateAvg)

        val viewModel = ViewModelProvider(
            this as ViewModelStoreOwner,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(WorkerHomeViewModel::class.java)

        viewModel.setHeartRateAvg(heartRateAvg)
    }

    companion object {
        private const val TAG = "HeartRateListenerService"
    }
}