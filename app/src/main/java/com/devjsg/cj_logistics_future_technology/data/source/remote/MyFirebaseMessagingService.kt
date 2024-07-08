package com.devjsg.cj_logistics_future_technology.data.source.remote

import android.util.Log
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
        remoteMessage.data.isNotEmpty().let {
            Log.d(TAG, "Message data payload: ${remoteMessage.data}")
            sendNotificationToWearable(remoteMessage.data)
        }

        // 알림 페이로드가 있는 경우
        remoteMessage.notification?.body?.let { body ->
            Log.d(TAG, "Message Notification Body: $body")
            sendNotificationToWearable(mapOf("body" to body))
        }
    }

    private fun sendNotificationToWearable(data: Map<String, String>) {
        // Wearable로 알림 전송하는 로직을 추가합니다.
        val notificationMessage = data["body"] ?: "No message body"
        WearableNotificationSender.sendNotification(this, notificationMessage)
    }

    companion object {
        private const val TAG = "MyFirebaseMsgService"
    }
}