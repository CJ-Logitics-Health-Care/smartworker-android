package com.devjsg.watch.data

import android.annotation.SuppressLint
import android.content.Context
import com.google.android.gms.wearable.DataClient
import com.google.android.gms.wearable.PutDataMapRequest
import com.google.android.gms.wearable.Wearable
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class DataSyncManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val dataClient: DataClient = Wearable.getDataClient(context)

    @SuppressLint("VisibleForTests")
    fun sendDataToPhone(heartRate: Int) {
        val putDataMapReq = PutDataMapRequest.create("/heart_rate").apply {
            dataMap.putInt("heart_rate", heartRate)
        }
        val putDataReq = putDataMapReq.asPutDataRequest()
        dataClient.putDataItem(putDataReq)
    }
}