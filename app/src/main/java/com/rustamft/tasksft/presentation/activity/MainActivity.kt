package com.rustamft.tasksft.presentation.activity

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import com.rustamft.tasksft.R
import com.rustamft.tasksft.permission.isNotificationPermissionGranted
import com.rustamft.tasksft.permission.requestNotificationPermission
import com.rustamft.tasksft.presentation.global.NOTIFICATION_CHANNEL_ID_TASK

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannelIfNeeded()
        }
        if (!isNotificationPermissionGranted()) {
            requestNotificationPermission()
        }
        setContent { MainActivityContent() }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannelIfNeeded() {
        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(
            NotificationChannel(
                NOTIFICATION_CHANNEL_ID_TASK,
                getString(R.string.task_notification_channel_name),
                NotificationManager.IMPORTANCE_HIGH,
            ).apply {
                description = getString(R.string.task_notification_channel_description)
            },
        )
    }
}
