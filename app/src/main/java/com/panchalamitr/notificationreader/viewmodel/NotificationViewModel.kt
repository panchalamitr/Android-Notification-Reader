package com.panchalamitr.notificationreader.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.panchalamitr.notificationreader.db.NotificationDb
import com.panchalamitr.notificationreader.model.NotificationDao
import com.panchalamitr.notificationreader.model.NotificationEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotificationViewModel(application: Application) : AndroidViewModel(application) {

    private val notificationDao: NotificationDao
    val notificationList: LiveData<List<NotificationEntity>>

    init {
        val database = NotificationDb.getInstance(application)
        notificationDao = database.notificationDao()
        notificationList = notificationDao.getNotifications()
    }
}
