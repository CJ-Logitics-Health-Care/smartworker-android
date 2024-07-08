package com.devjsg.cj_logistics_future_technology.data.source.remote

import android.app.NotificationManager
import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat
import com.devjsg.cj_logistics_future_technology.MyApplication
import com.devjsg.cj_logistics_future_technology.R
import com.google.android.gms.wearable.DataEvent
import com.google.android.gms.wearable.DataEventBuffer
import com.google.android.gms.wearable.DataMapItem
import com.google.android.gms.wearable.WearableListenerService

class MyWearableListenerService : WearableListenerService() {

    override fun onDataChanged(dataEvents: DataEventBuffer) {
        for (event in dataEvents) {
            if (event.type == DataEvent.TYPE_CHANGED) {
                val dataItem = event.dataItem
                if (dataItem.uri.path == "/notification") {
                    val dataMap = DataMapItem.fromDataItem(dataItem).dataMap
                    val message = dataMap.getString("message")
                    Log.d(TAG, "Received notification on wearable: $message")
                    showNotification(message)
                }
            }
        }
    }

    private fun showNotification(message: String?) {
        Log.d(TAG, "showNotification: $message")
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification = NotificationCompat.Builder(this, MyApplication.CHANNEL_ID)
            .setContentTitle("New Notification")
            .setContentText(message)
            .setSmallIcon(R.drawable.splash_icon)  // 적절한 아이콘 리소스로 변경하세요.
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()
        notificationManager.notify(1, notification)
    }

    companion object {
        private const val TAG = "MyWearableListenerService"
    }
}