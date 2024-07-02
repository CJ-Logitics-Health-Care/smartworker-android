package com.devjsg.cj_logistics_future_technology.presentation.home

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.devjsg.cj_logistics_future_technology.R
import com.devjsg.cj_logistics_future_technology.data.datasync.DataSyncManager
import com.google.android.gms.wearable.DataClient
import com.google.android.gms.wearable.DataEvent
import com.google.android.gms.wearable.DataEventBuffer
import com.google.android.gms.wearable.DataMapItem
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DataSyncService : Service(), DataClient.OnDataChangedListener {

    @Inject
    lateinit var dataClient: DataClient

    @Inject
    lateinit var dataSyncManager: DataSyncManager

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "DataSyncService started and listener added")

        createNotificationChannelIfNeeded()

        // 알림 생성
        val notification = NotificationCompat.Builder(this, "DATA_SYNC_CHANNEL")
            .setContentTitle("Data Sync Service")
            .setContentText("Synchronizing data in background")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .build()

        // 포어그라운드 서비스로 시작
        startForeground(1, notification)

        dataClient.addListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        dataClient.removeListener(this)
        Log.d(TAG, "DataSyncService stopped and listener removed")
    }

    private fun createNotificationChannelIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannelId = "DATA_SYNC_CHANNEL"
            val channel = NotificationChannel(
                notificationChannelId,
                "Data Sync Service",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    override fun onDataChanged(dataEvents: DataEventBuffer) {
        Log.d(TAG, "onDataChanged called")
        for (event in dataEvents) {
            if (event.type == DataEvent.TYPE_CHANGED) {
                val dataItem = event.dataItem
                Log.d(TAG, "DataEvent type changed for path: ${dataItem.uri.path}")
                if (dataItem.uri.path == "/heart_rate") {
                    val dataMap = DataMapItem.fromDataItem(dataItem).dataMap
                    val heartRate = dataMap.getInt("heartRate")
                    val timestamp = dataMap.getLong("timestamp")
                    Log.d(TAG, "Received heart rate data: $heartRate at $timestamp")

                    // 업데이트가 DataSyncManager로 전달되었는지 확인합니다.
                    dataSyncManager.updateHeartRate(heartRate)
                }
            }
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    companion object {
        private const val TAG = "DataSyncService"
    }
}