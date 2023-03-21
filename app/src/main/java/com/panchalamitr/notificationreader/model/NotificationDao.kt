package com.panchalamitr.notificationreader.model

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface NotificationDao {

    @Query("SELECT * FROM notification_table ORDER BY timestamp DESC")
    fun getNotifications(): LiveData<List<NotificationEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertNotification(notification: NotificationEntity)

    @Query("SELECT * FROM notification_table WHERE packageName = :packageName AND title = :title AND message = :message AND timestamp >= :minPostTime")
    suspend fun getNotificationByTitlePackageNameAndMessage(packageName: String, title: String, message: String, minPostTime: Long): NotificationEntity?

    @Query("DELETE FROM notification_table")
    suspend fun deleteAllNotifications()
}
