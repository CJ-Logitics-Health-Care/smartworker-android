package com.devjsg.watch.domain

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.android.gms.wearable.PutDataMapRequest
import com.google.android.gms.wearable.Wearable

class SendHeartRateAvgWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val heartRateAvg = inputData.getInt("heartRateAvg", 0)

        // 스마트폰에 데이터를 전송하는 로직을 여기에 구현합니다.
        sendHeartRateAvgToPhone(heartRateAvg)

        return Result.success()
    }

    @SuppressLint("VisibleForTests")
    private fun sendHeartRateAvgToPhone(heartRateAvg: Int) {
        val putDataMapRequest = PutDataMapRequest.create("/heart_rate_avg").apply {
            dataMap.putInt("heartRateAvg", heartRateAvg)
            dataMap.putLong("timestamp", System.currentTimeMillis())
        }
        val request = putDataMapRequest.asPutDataRequest()
        val dataClient = Wearable.getDataClient(applicationContext)
        dataClient.putDataItem(request)
            .addOnSuccessListener {
                Log.d("SendHeartRateAvgWorker", "Successfully sent heart rate avg data to phone: $heartRateAvg")
            }
            .addOnFailureListener { e ->
                Log.e("SendHeartRateAvgWorker", "Failed to send heart rate avg data to phone", e)
            }
    }
}