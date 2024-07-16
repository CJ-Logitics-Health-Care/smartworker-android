package com.devjsg.cj_logistics_future_technology.data.source.remote

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.devjsg.cj_logistics_future_technology.R
import com.devjsg.cj_logistics_future_technology.presentation.main.MainActivity
import com.devjsg.cj_logistics_future_technology.presentation.main.MyApplication
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "FCM token: $token")
        // 여기서 서버에 토큰을 저장하는 로직 설정
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d(TAG, "From: ${remoteMessage.from}")

        // 데이터 페이로드가 있는 경우
        if (remoteMessage.data.isNotEmpty()) {
            Log.d(TAG, "Message data payload: ${remoteMessage.data}")
            sendNotificationToWearable(remoteMessage.data)
            sendNotification(remoteMessage.data)
        }

        // 알림 페이로드가 있는 경우
        remoteMessage.notification?.body?.let { body ->
            Log.d(TAG, "Message Notification Body: $body")
            sendNotificationToWearable(mapOf("body" to body))
            sendNotification(mapOf("body" to body))
        }
    }

    private fun sendNotificationToWearable(data: Map<String, String>) {
        // Wearable로 알림 전송하는 로직을 추가합니다.
        val notificationMessage = data["body"] ?: "No message body"
        WearableNotificationSender.sendNotification(this, notificationMessage)
    }

    private fun sendNotification(data: Map<String, String>) {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("navigateTo", "maps")
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val notificationBuilder = NotificationCompat.Builder(this, MyApplication.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("FCM Message")
            .setContentText(data["body"])
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0, notificationBuilder.build())
    }

    companion object {
        private const val TAG = "MyFirebaseMsgService"
    }
}