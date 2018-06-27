package com.example.duc25.modules

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import com.example.duc25.activity.MainActivity
import com.example.duc25.activity.R

class HomeNotification(val context: Context) {

    fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= 26){
            val name  = "home"
            val description = "description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val chanel = NotificationChannel("home", name, importance)
            chanel.description = description
            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(chanel)
        }
    }

    fun notification(data: String){
        val intent = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)

        val mBuilder = NotificationCompat.Builder(context, "home")
                .setSmallIcon(R.drawable.ic_launcher_round)
                .setContentTitle("Trạng thái")
                //.setContentText(data)
                .setStyle(NotificationCompat.BigTextStyle().bigText(data))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setOngoing(true)

        NotificationManagerCompat.from(context)
                .notify(1, mBuilder.build())
    }
}