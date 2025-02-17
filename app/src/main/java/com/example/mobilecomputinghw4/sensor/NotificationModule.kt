package com.example.mobilecomputinghw4.sensor

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.os.Build
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import com.example.mobilecomputinghw4.R


const val CHANNEL_ID = "Main Channel ID"
const val NOTIFICATION_ID = 1


fun createNotificationChannel(context: Context) {
    // Create the NotificationChannel, but only on API 26+ because
    // the NotificationChannel class is not in the Support Library.
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Main Channel",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "Main notification channel"
        }

        getSystemService(context, NotificationManager::class.java)
        // Register the channel with the system.
        val notificationManager: NotificationManager =
            getSystemService(context, NotificationManager::class.java) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}

fun createNotificationBuilder(
    context: Context,
    textTitle: String,
    textContent: String
): NotificationCompat.Builder {
    return NotificationCompat.Builder(context, CHANNEL_ID)
        .setSmallIcon(R.drawable.speed_notification_icon)
        .setContentTitle(textTitle)
        .setContentText(textContent)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
}

//fun setNotificationTapAction(
//    context: Context,
//    textTitle: String,
//    textContent: String
//) {
//    // Create an explicit intent for an Activity in your app.
//    val intent = Intent(context, AlertDetails::class.java).apply {
//        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//    }
//    val pendingIntent: PendingIntent =
//        PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
//
//    val builder = createNotificationBuilder(context, textTitle, textContent)
//        // Set the intent that fires when the user taps the notification.
//        .setContentIntent(pendingIntent)
//        .setAutoCancel(true)
//}
//
//fun showNotification(context: Context, builder: NotificationCompat.Builder) {
//    with(NotificationManagerCompat.from(context)) {
//        if (ActivityCompat.checkSelfPermission(
//                this@MainActivity,
//                Manifest.permission.POST_NOTIFICATIONS
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            // TODO: Consider calling
//            // ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            // public fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
//            //                                        grantResults: IntArray)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//
//            return@with
//        }
//        // notificationId is a unique int for each notification that you must define.
//        notify(NOTIFICATION_ID, builder.build())
//    }
//
//}


fun provideNotificationManager(context: Context): NotificationManagerCompat {
    val notificationManager = NotificationManagerCompat.from(context)

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Main Channel",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "Main notification channel"
        }
        notificationManager.createNotificationChannel(channel)
    }
    return notificationManager
}

