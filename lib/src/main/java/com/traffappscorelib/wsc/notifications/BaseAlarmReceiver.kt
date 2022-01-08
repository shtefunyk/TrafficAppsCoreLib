package com.traffappscorelib.wsc.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.traffappscorelib.wsc.R
import com.traffappscorelib.wsc.StartActivity
import com.traffappscorelib.wsc.data.Preferences

abstract class BaseAlarmReceiver : BroadcastReceiver() {

    abstract fun getIconRes() : Int

    private val NOTIFICATION_ID = "10010"

    override fun onReceive(context: Context, intent: Intent) {
        val preferences = Preferences(context.getSharedPreferences(
            Preferences.PREFS_NAME,
            Context.MODE_PRIVATE
        ))

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val mChannel = NotificationChannel(
                NOTIFICATION_ID,
                "Robin",
                importance
            )
            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(mChannel)
        }

        val resultIntent = Intent(context, StartActivity::class.java)
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_MUTABLE)

        val notificationBuilder = NotificationCompat.Builder(context, NOTIFICATION_ID)
            .setSmallIcon(getIconRes())
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setContentTitle(preferences.notificationTitle)
            .setContentText(preferences.notificationText)
            .setColor(ContextCompat.getColor(context, R.color.white))
            .setContentIntent(pendingIntent)

        with(NotificationManagerCompat.from(context)) {
            notify(717, notificationBuilder.build())
        }
    }
}