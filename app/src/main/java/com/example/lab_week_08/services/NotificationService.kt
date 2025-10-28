package com.example.lab_week_08.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.example.lab_week_08.R

class NotificationService : Service() {

    private val channelId = "001"
    private val channelName = "LAB_WEEK_08 Channel"

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        showCountdownNotification()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Channel untuk Worker Notification"
                enableVibration(true)
            }
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    private fun showCountdownNotification() {
        val manager = getSystemService(NotificationManager::class.java)
        val notificationId = 1

        Thread {
            for (i in 10 downTo 0) {
                val notification: Notification = NotificationCompat.Builder(this, channelId)
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentTitle("Second worker process is done")
                    .setContentText("$i seconds until last warning")
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setOngoing(true)
                    .build()

                manager.notify(notificationId, notification)
                Thread.sleep(1000)
            }

            manager.cancel(notificationId)
            Handler(Looper.getMainLooper()).post {
                Toast.makeText(this, "Channel id 001 process is done!", Toast.LENGTH_LONG).show()
            }

            stopSelf()
        }.start()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
