package com.panchalamitr.notificationreader

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.panchalamitr.notificationreader.adapter.NotificationAdapter
import com.panchalamitr.notificationreader.databinding.ActivityMainBinding
import com.panchalamitr.notificationreader.model.NotificationEntity
import com.panchalamitr.notificationreader.service.NotificationListener
import com.panchalamitr.notificationreader.viewmodel.NotificationViewModel


class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    private lateinit var binding: ActivityMainBinding
    private lateinit var notificationAdapter: NotificationAdapter
    private lateinit var notificationViewModel: NotificationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding.root)
        insertIntoFirebaseStorage()
        val intent = Intent(this, NotificationListener::class.java)
        startService(intent)
    }

   private fun requestNotificationAccessPermission() {
        val result = isNotificationAccessPermissionGranted()
        if (!result) {
            // permission not granted, show message and request permission
            val builder: AlertDialog.Builder = AlertDialog.Builder(this)
            builder.setTitle("Notification Access Permission Required")
            builder.setMessage("To read all notifications, this app requires the Notification Access Permission. Please grant the permission in the next screen.")
            builder.setPositiveButton("Grant Permission"
            ) { dialog, which ->
                val intent = Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS")
                startActivity(intent)
            }
            builder.setNegativeButton("Cancel", null)
            builder.show()
        } else {
            initRecyclerView()
        }

    }

     /** Below function will store data in firebase storage **/
    private fun insertIntoFirebaseStorage() {
         val firestore = FirebaseFirestore.getInstance()
         val collectionRef = firestore.collection("notifications")
         val notification = NotificationEntity(
             packageName = "com.example.app",
             title = "New Notification",
             message = "Hello World",
             timestamp = System.currentTimeMillis(),
             appName = "My App"
         )
         collectionRef.document().set(notification)
     }

    override fun onResume() {
        super.onResume()
        requestNotificationAccessPermission()
    }
    private fun initRecyclerView() {
        // Initialize the RecyclerView
        notificationAdapter = NotificationAdapter(this)
        binding.notificationList.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = notificationAdapter
        }

        // Initialize the NotificationViewModel
        notificationViewModel = ViewModelProvider(this)[NotificationViewModel::class.java]

        // Observe changes to the list of notifications in the ViewModel
        notificationViewModel.notificationList.observe(this) { notifications ->
            // Update the RecyclerView with the new list of notifications
            notificationAdapter.submitList(notifications)
            binding.notificationList.smoothScrollToPosition(0)
        }
    }

    private fun isNotificationAccessPermissionGranted(): Boolean {
        val packageName = packageName
        val flat: String =
            Settings.Secure.getString(contentResolver, "enabled_notification_listeners")
        val policies = flat.split(":")
            .toTypedArray()
        for (i in policies.indices) {
            val componentName = ComponentName.unflattenFromString(policies[i])
            Log.d(TAG, "isNotificationAccessPermissionGranted: " + componentName?.packageName)
            if (componentName != null && TextUtils.equals(
                    packageName,
                    componentName.packageName
                )
            ) {
                return true
            }
        }
        return false
    }

}