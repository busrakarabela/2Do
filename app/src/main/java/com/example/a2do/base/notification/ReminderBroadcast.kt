package com.example.a2do.base.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.a2do.R
import com.example.a2do.base.notification.NotificationService.Companion.CHANNEL_ID

class ReminderBroadcast: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {


        val builder = NotificationCompat.Builder(context!!, CHANNEL_ID)
            .setSmallIcon(R.drawable.icon_alarm)
            .setContentTitle("Etkinlik Bildirimi")
            .setContentText("Yaklaşan bir etkinliğiniz var")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            // Set the intent that will fire when the user taps the notification
            .setAutoCancel(true)


        val id:Int=Math.random().toInt()
        with(NotificationManagerCompat.from(context!!)) {
            // notificationId is a unique int for each notification that you must define
            notify(id, builder.build())
    }
    }


}