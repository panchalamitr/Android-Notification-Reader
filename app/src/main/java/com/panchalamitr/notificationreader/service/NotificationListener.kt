package com.panchalamitr.notificationreader.service

import android.app.Notification
import android.content.pm.PackageManager
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import com.panchalamitr.notificationreader.db.NotificationDb
import com.panchalamitr.notificationreader.model.NotificationDao
import com.panchalamitr.notificationreader.model.NotificationEntity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class NotificationListener : NotificationListenerService() {

    private lateinit var notificationDao: NotificationDao
    override fun onCreate() {
        super.onCreate()
        notificationDao = NotificationDb.getInstance(this).notificationDao()
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        if (sbn.tag != null) {
            val packageName = sbn.packageName
            val title = sbn.notification.extras.getString(Notification.EXTRA_TITLE)
            val message = sbn.notification.extras.getString(Notification.EXTRA_TEXT)?.toString()
            val timestamp = sbn.postTime
            val appName = getAppName(packageName)

            val notificationEntity = NotificationEntity(
                packageName = packageName,
                title = title,
                message = message,
                timestamp = timestamp,
                appName = appName
            )

            GlobalScope.launch {
                notificationDao.insertNotification(notificationEntity)
            }
        }
    }



    private fun getAppName(packageName: String): String? {
        return try {
            val applicationInfo = packageManager.getApplicationInfo(packageName, 0)
            packageManager.getApplicationLabel(applicationInfo).toString()
        } catch (e: PackageManager.NameNotFoundException) {
            null
        }
    }

}
