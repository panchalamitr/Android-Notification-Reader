package com.panchalamitr.notificationreader.model

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import java.io.ByteArrayOutputStream

@Entity(tableName = "notification_table")
data class NotificationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val packageName: String,
    val title: String?,
    val message: String?,
    val timestamp: Long,
    val appName: String?
)


