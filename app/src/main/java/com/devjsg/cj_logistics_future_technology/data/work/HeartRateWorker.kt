package com.devjsg.cj_logistics_future_technology.data.work

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.devjsg.cj_logistics_future_technology.data.network.HeartRateApiService
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@HiltWorker
class HeartRateWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val heartRateApiService: HeartRateApiService
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        val heartRateAvg = inputData.getInt("heartRateAvg", -1)
        if (heartRateAvg == -1) {
            return@withContext Result.failure()
        }

        return@withContext try {
            heartRateApiService.sendHeartRateData(heartRateAvg)
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }

    companion object {
        fun enqueueWork(context: Context, heartRateAvg: Int) {
            val workRequest = OneTimeWorkRequestBuilder<HeartRateWorker>()
                .setInputData(workDataOf("heartRateAvg" to heartRateAvg))
                .build()
            WorkManager.getInstance(context).enqueue(workRequest)
        }
    }
}