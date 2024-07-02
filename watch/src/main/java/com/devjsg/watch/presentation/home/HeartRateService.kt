package com.devjsg.watch.presentation.home

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.os.PowerManager
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.devjsg.watch.R
import com.devjsg.watch.data.HeartRateRepository
import com.devjsg.watch.data.MeasureMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HeartRateService : Service() {

    @Inject
    lateinit var heartRateRepository: HeartRateRepository

    private val serviceScope = CoroutineScope(Dispatchers.Main + Job())
    private lateinit var wakeLock: PowerManager.WakeLock

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "Service onCreate")
        acquireWakeLock()
        startForegroundService()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "Service onStartCommand")
        startMonitoring()
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
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
            .setSmallIcon(R.drawable.baseline_monitor_heart_24)
            .build()

        startForeground(1, notification)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun startMonitoring() {
        Log.d(TAG, "Starting heart rate monitoring")
        serviceScope.launch {
            heartRateRepository.heartRateMeasureFlow().collect { message ->
                when (message) {
                    is MeasureMessage.MeasureData -> {
                        val heartRateValue = message.data.first().value.toInt()
                        Log.d(TAG, "💓 Heart Rate: $heartRateValue")
                        sendHeartRateBroadcast(heartRateValue)
                    }
                    is MeasureMessage.MeasureAvailability -> {
                        // Handle availability change if needed
                    }
                }
            }
        }
    }

    private fun sendHeartRateBroadcast(heartRate: Int) {
        val intent = Intent("com.devjsg.watch.HEART_RATE_UPDATE")
        intent.putExtra("heartRate", heartRate)
        sendBroadcast(intent)
    }

    private fun stopMonitoring() {
        Log.d(TAG, "Stopping heart rate monitoring")
        serviceScope.coroutineContext.cancelChildren()
    }

    private fun acquireWakeLock() {
        val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
        wakeLock = powerManager.newWakeLock(
            PowerManager.SCREEN_DIM_WAKE_LOCK or PowerManager.ACQUIRE_CAUSES_WAKEUP,
            "HeartRateService::WakeLock"
        )
        wakeLock.acquire()
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