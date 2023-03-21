package com.panchalamitr.notificationreader.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.panchalamitr.notificationreader.model.NotificationDao
import com.panchalamitr.notificationreader.model.NotificationEntity

@Database(entities = [NotificationEntity::class], version = 1)
abstract class NotificationDb : RoomDatabase() {

    abstract fun notificationDao(): NotificationDao

    companion object {
        private const val DATABASE_NAME = "notification_database"

        @Volatile
        private var instance: NotificationDb? = null

        fun getInstance(context: Context): NotificationDb {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): NotificationDb {
            return Room.databaseBuilder(
                context.applicationContext,
                NotificationDb::class.java,
                DATABASE_NAME
            ).build()
        }
    }
}
