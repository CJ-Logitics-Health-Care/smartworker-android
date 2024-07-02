package com.devjsg.cj_logistics_future_technology.data.datasync

import android.util.Log
import com.google.android.gms.wearable.DataClient
import com.google.android.gms.wearable.DataEvent
import com.google.android.gms.wearable.DataEventBuffer
import com.google.android.gms.wearable.DataMapItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class DataSyncManager @Inject constructor(
    private val dataClient: DataClient
) : DataClient.OnDataChangedListener {

    private val _heartRate = MutableStateFlow(0)
    val heartRate: StateFlow<Int> get() = _heartRate

    init {
        dataClient.addListener(this)
        Log.d(TAG, "DataSyncManager initialized and listener added")
    }

    override fun onDataChanged(dataEvents: DataEventBuffer) {
        Log.d(TAG, "DataSyncManager onDataChanged called")
        for (event in dataEvents) {
            if (event.type == DataEvent.TYPE_CHANGED) {
                val dataItem = event.dataItem
                if (dataItem.uri.path == "/heart_rate") {
                    val dataMap = DataMapItem.fromDataItem(dataItem).dataMap
                    val heartRate = dataMap.getInt("heartRate")
                    Log.d(TAG, "DataSyncManager received heart rate data: $heartRate")
                    _heartRate.value = heartRate
                }
            }
        }
    }

    fun updateHeartRate(heartRate: Int) {
        Log.d(TAG, "Updating heart rate in DataSyncManager: $heartRate")
        _heartRate.value = heartRate
    }

    fun cleanup() {
        dataClient.removeListener(this)
        Log.d(TAG, "DataSyncManager listener removed")
    }

    companion object {
        private const val TAG = "DataSyncManager"
    }
}