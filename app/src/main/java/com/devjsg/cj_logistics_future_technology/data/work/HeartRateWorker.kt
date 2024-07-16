package com.devjsg.cj_logistics_future_technology.data.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.devjsg.cj_logistics_future_technology.data.local.datastore.DataStoreManager
import com.devjsg.cj_logistics_future_technology.data.network.NetworkConstants
import com.devjsg.cj_logistics_future_technology.di.worker.ChildWorkerFactory
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import io.ktor.client.HttpClient
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import kotlinx.coroutines.flow.first
import kotlinx.serialization.Serializable

class HeartRateWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val dataStoreManager: DataStoreManager,
    private val httpClient: HttpClient
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val heartRateAvg = inputData.getInt(EXTRA_HEART_RATE_AVG, 0)

        val token = dataStoreManager.token.first()

        val requestBody = HeartRateRequest(heartRateAvg)

        return try {
            val response = httpClient.post(NetworkConstants.BASE_URL + "heart-rate") {
                headers {
                    append("Authorization", "Bearer $token")
                    append("Content-Type", "application/json")
                }
                setBody(requestBody)
            }

            if (response.status.value in 200..299) {
                Result.success()
            } else {
                Result.failure()
            }
        } catch (e: Exception) {
            Result.failure()
        }
    }

    @Serializable
    data class HeartRateRequest(val heartRateAvg: Int)

    @AssistedFactory
    interface Factory : ChildWorkerFactory {
        override fun create(appContext: Context, params: WorkerParameters): HeartRateWorker
    }

    companion object {
        const val EXTRA_HEART_RATE_AVG =
            "com.devjsg.cj_logistics_future_technology.EXTRA_HEART_RATE_AVG"

        fun enqueueWork(context: Context, heartRateAvg: Int) {
            val workRequest = OneTimeWorkRequestBuilder<HeartRateWorker>()
                .setInputData(workDataOf(EXTRA_HEART_RATE_AVG to heartRateAvg))
                .build()
            WorkManager.getInstance(context).enqueue(workRequest)
        }
    }
}