package com.devjsg.cj_logistics_future_technology.data.source.remote

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import android.os.PowerManager
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.devjsg.cj_logistics_future_technology.R
import com.devjsg.cj_logistics_future_technology.data.repository.HeartRateRepository
import com.devjsg.cj_logistics_future_technology.data.repository.MeasureMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HeartRateService : Service() {

    @Inject
    lateinit var heartRateRepository: HeartRateRepository

    private val serviceScope = CoroutineScope(Dispatchers.Main + Job())
    private lateinit var wakeLock: PowerManager.WakeLock

    private val heartRateList = mutableListOf<Int>()

    private var stepCount = 0
    

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "Service onCreate")
        val filter = IntentFilter("com.devjsg.watch.STEP_COUNT_UPDATE")
        registerReceiver(stepCountReceiver, filter, RECEIVER_NOT_EXPORTED)
        acquireWakeLock()
        startForegroundService()

        serviceScope.launch {
            startMonitoring()
        }
    }

    private val stepCountReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.let {
                if (it.action == "com.devjsg.watch.STEP_COUNT_UPDATE") {
                    val stepCount = it.getIntExtra("stepCount", 0)
                    Log.d(TAG, "Received step count: $stepCount")
                }
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "Service onStartCommand")
        startMonitoring()
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(stepCountReceiver)
        Log.d(TAG, "Service onDestroy")
        stopMonitoring()
        releaseWakeLock()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    @SuppressLint("ForegroundServiceType")
    private fun startForegroundService() {
        Log.d(TAG, "Starting foreground service")
        val notificationChannelId = "HEART_RATE_CHANNEL"
        val channel = NotificationChannel(
            notificationChannelId,
            "Heart Rate Service",
            NotificationManager.IMPORTANCE_LOW
        )

        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)

        val notification: Notification = NotificationCompat.Builder(this, notificationChannelId)
            .setContentTitle("Heart Rate Monitoring")
            .setContentText("Monitoring heart rate in background")
            .setSmallIcon(R.drawable.splash_icon)
            .build()

        startForeground(1, notification)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun startMonitoring() {
        serviceScope.launch {
            heartRateRepository.heartRateMeasureFlow().collect { message ->
                when (message) {
                    is MeasureMessage.MeasureData -> {
                        val heartRateValue = message.data.first().value.toInt()
                        Log.d(TAG, "Heart Rate Value: $heartRateValue")
                        heartRateList.add(heartRateValue)
                    }

                    is MeasureMessage.MeasureAvailability -> {
                        //Log.d(TAG, "Heart Rate Sensor Availability: ${message.isAvailable}")
                    }
                }
            }
        }

        serviceScope.launch {
            while (true) {
                delay(5 * 1000)
                val avgHeartRate = calculateAverageHeartRate()
                if (avgHeartRate != 0) {
                    Log.d(TAG, "Average Heart Rate: $avgHeartRate, Step Count: $stepCount")
                    sendDataToDataLayer(avgHeartRate, stepCount)
                }
                heartRateList.clear()
            }
        }
    }

    private fun sendDataToDataLayer(heartRateAvg: Int, stepCount: Int) {
        val data = Data.Builder()
            .putInt("heartRateAvg", heartRateAvg)
            .putInt("stepCount", stepCount)
            .build()

        val sendDataWorkRequest = OneTimeWorkRequestBuilder<SendHeartRateAvgWorker>()
            .setInputData(data)
            .build()

        WorkManager.getInstance(this).enqueue(sendDataWorkRequest)
    }

    private fun calculateAverageHeartRate(): Int {
        val validHeartRates = heartRateList.filter { it != 0 }
        return if (validHeartRates.isNotEmpty()) {
            validHeartRates.sum() / validHeartRates.size
        } else {
            0
        }
    }

    /*@OptIn(ExperimentalCoroutinesApi::class)
    private fun startMonitoring() {
        Log.d(TAG, "Starting heart rate monitoring")
        serviceScope.launch {
            heartRateRepository.heartRateMeasureFlow().collect { message ->
                when (message) {
                    is MeasureMessage.MeasureData -> {
                        val heartRateValue = message.data.first().value.toInt()
                        Log.d(TAG, "💓 Heart Rate: $heartRateValue")
                        heartRateList.add(heartRateValue)
                        sendHeartRateBroadcast(heartRateValue)
                    }
                    is MeasureMessage.MeasureAvailability -> {
                        // Handle availability change if needed
                    }
                }
            }
        }

        serviceScope.launch {
            while (true) {
                delay(5 * 1000)
                val avg = calculateAverageHeartRate()
                if(avg != 0){
                    sendHeartRateAvgBroadcast(avg)
                    scheduleHeartRateAvgWork(avg)
                }
                heartRateList.clear()
            }
        }
    }*/

    /*private fun calculateAverageHeartRate(): Int {
        val validHeartRates = heartRateList.filter { it != 0 }
        return if (validHeartRates.isNotEmpty()) {
            validHeartRates.sum() / validHeartRates.size
        } else {
            0
        }
    }*/

    private fun sendHeartRateBroadcast(heartRate: Int) {
        val intent = Intent("com.devjsg.watch.HEART_RATE_UPDATE")
        intent.putExtra("heartRate", heartRate)
        sendBroadcast(intent)
    }

    private fun sendHeartRateAvgBroadcast(heartRateAvg: Int) {
        val intent = Intent("com.devjsg.watch.HEART_RATE_AVG_UPDATE")
        intent.putExtra("heartRateAvg", heartRateAvg)
        sendBroadcast(intent)
    }
    private fun scheduleHeartRateAvgWork(heartRateAvg: Int) {
        val data = workDataOf("heartRateAvg" to heartRateAvg)
        val workRequest = OneTimeWorkRequestBuilder<SendHeartRateAvgWorker>()
            .setInputData(data)
            .build()
        WorkManager.getInstance(this).enqueue(workRequest)
    }

    private fun stopMonitoring() {
        Log.d(TAG, "Stopping heart rate monitoring")
        serviceScope.coroutineContext.cancelChildren()
    }

    private fun acquireWakeLock() {
        val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
        wakeLock = powerManager.newWakeLock(
            PowerManager.PARTIAL_WAKE_LOCK,
            "HeartRateService::WakeLock"
        )
        wakeLock.acquire(10 * 60 * 1000L )
        Log.d(TAG, "WakeLock acquired")
    }

    private fun releaseWakeLock() {
        if (this::wakeLock.isInitialized && wakeLock.isHeld) {
            wakeLock.release()
            Log.d(TAG, "WakeLock released")
        }
    }

    companion object {
        private const val TAG = "HeartRateService"

        fun startService(context: Context) {
            Log.d(TAG, "Starting HeartRateService")
            val intent = Intent(context, HeartRateService::class.java)
            ContextCompat.startForegroundService(context, intent)
        }

        fun stopService(context: Context) {
            Log.d(TAG, "Stopping HeartRateService")
            val intent = Intent(context, HeartRateService::class.java)
            context.stopService(intent)
        }
    }
}