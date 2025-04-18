package com.example.constructionmanagement.viewmodel

import android.Manifest
import android.app.NotificationChannel
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import android.util.Log
import android.app.NotificationManager
import androidx.core.app.NotificationCompat
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import com.example.constructionmanagement.R

class MyFCM : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        remoteMessage.notification?.let {
            val title = it.title ?: "New Task!"
            val message = it.body ?: " You have a new daily task."
            showNotification(title, message)
        }
    }

    private fun showNotification(title: String, messageBody: String) {
        val channelId = "firebase_notifications"

        // Channel for Android 0 and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Task Notifications", NotificationManager.IMPORTANCE_HIGH)
            getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.notifications_24px)
            .setContentTitle(title)
            .setContentText(messageBody)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        // Android 13+ notification permissions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(
                    this, Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                Log.w("FCM", "Notification permission not granted!")
                return
            }
        }

        NotificationManagerCompat.from(this).notify(0, notification)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        // New token to Firebase for registration
        Log.d("FCM Token", "New token: $token")
    }
}
