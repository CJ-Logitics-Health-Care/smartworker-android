package com.devjsg.cj_logistics_future_technology.presentation.service

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Handler
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.devjsg.cj_logistics_future_technology.R
import com.devjsg.cj_logistics_future_technology.data.source.remote.SendHeartRateAvgWorker
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class StepCounterService : Service(), SensorEventListener {

    @Inject
    lateinit var sensorManager: SensorManager

    @Inject
    lateinit var handler: Handler

    private var stepCounterSensor: Sensor? = null
    private var stepCount: Int = 0
    private var stepsIn5Sec: Int = 0

    override fun onCreate() {
        super.onCreate()

        stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)

        stepCounterSensor?.also { stepSensor ->
            sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_NORMAL)
        }

        startForegroundService()
        handler.postDelayed(stepRunnable, 5000)
    }

    @SuppressLint("ForegroundServiceType")
    private fun startForegroundService() {
        val notificationChannelId = "STEP_COUNTER_CHANNEL"
        val channel = NotificationChannel(
            notificationChannelId,
            "Step Counter Service",
            NotificationManager.IMPORTANCE_LOW
        )

        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)

        val notification: Notification = NotificationCompat.Builder(this, notificationChannelId)
            .setContentTitle("Step Counter Service")
            .setContentText("Monitoring steps in the background")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .build()

        startForeground(1, notification) // startForeground 호출
    }

    private fun sendStepCountToDataLayer() {
        val randomStepCount = (1..10).random() // 1~10 사이의 난수 생성
        val data = Data.Builder()
            .putInt("stepCount", randomStepCount)
            .build()

        val sendStepCountWorkRequest = OneTimeWorkRequestBuilder<SendHeartRateAvgWorker>()
            .setInputData(data)
            .build()

        WorkManager.getInstance(this).enqueue(sendStepCountWorkRequest)
    }


    private val stepRunnable = object : Runnable {
        override fun run() {
            stepsIn5Sec = stepCount
            stepCount = 0
            Log.d("StepCounter", "Steps in last 5 seconds: $stepsIn5Sec")
            handler.postDelayed(this, 5000)
        }
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_STEP_DETECTOR) {
            stepCount++
            Log.d("StepCounterService", "걸음 수: $stepCount")
        }
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        sensorManager.unregisterListener(this)
        handler.removeCallbacks(stepRunnable)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}