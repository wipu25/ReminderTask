package com.example.remindertask.data.source.repo

import android.app.Notification
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.remindertask.R

const val channelID = "1"
const val notificationID = 1
const val titleExtra = "Title"
const val messageExtra = "Message"

class NotificationRepository: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val notification = Notification.Builder(context, channelID)
            .setSmallIcon(R.drawable.notification)
            .setContentTitle(intent?.getStringExtra(titleExtra))
            .setContentText(intent?.getStringExtra(messageExtra))
            .build()

        val manager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(notificationID, notification)
    }
}