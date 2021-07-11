package com.srs.lmpapp.notifications
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService

import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat

import com.google.firebase.messaging.RemoteMessage
import com.srs.lmpapp.R
import com.srs.lmpapp.utils.Constants

import kotlin.random.Random

private const val CHANNEL_ID="my_channel"
private const val TAG="FirebaseService"
class MyFirebaseMessagingService: FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        val intent = Intent(this, MyFirebaseMessagingService::class.java)
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationID = Random.nextInt()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager)
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(message.data["title"])

            .setContentText(message.data["body"])

            .setSmallIcon(R.drawable.ic_calendar)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()
        notificationManager.notify(notificationID, notification)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager) {

        val channelName = "My_channel"
        val channel = NotificationChannel(
            CHANNEL_ID,
            channelName,
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "my channel description"
            enableLights(true)
            lightColor = Color.GREEN
        }
        notificationManager.createNotificationChannel(channel)

    }



    override fun onNewToken(newToken: String) {
        //Log.d(TAG, "Refreshed token: $token")
        super.onNewToken(newToken)
        val sharedPreferences =getSharedPreferences(Constants.MYLMSAPP_PREFERENCES,  Context.MODE_PRIVATE )
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString(Constants.APP_TOKEN,newToken)
        editor.apply()


        //token = newToken
        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // FCM registration token to your app server.
        //sendRegistrationToServer(token)
    }
}