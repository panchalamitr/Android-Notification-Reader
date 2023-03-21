package com.panchalamitr.notificationreader.adapter

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.UserHandle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.panchalamitr.notificationreader.databinding.NotificationItemLayoutBinding
import com.panchalamitr.notificationreader.model.NotificationEntity
import androidx.recyclerview.widget.ListAdapter

class NotificationAdapter(val context: Context) :
    ListAdapter<NotificationEntity, NotificationAdapter.ViewHolder>(NotificationDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = NotificationItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val notification = currentList[position]
        holder.bind(notification)
    }

    inner class ViewHolder(private val binding: NotificationItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(notification: NotificationEntity) {

            binding.titleTextView.text = notification.title
            binding.messageTextView.text = notification.message
            binding.timestampTextView.text =
                DateFormat.format("dd MMM yyyy HH:mm:ss", notification.timestamp)
            binding.appNameTextView.text = notification.appName
            val icon: Drawable =
                context.packageManager.getApplicationIcon(notification.packageName)
            if (icon != null)
                binding.appIconImageView.setImageDrawable(icon)
        }
    }

    class NotificationDiffCallback : DiffUtil.ItemCallback<NotificationEntity>() {
        override fun areItemsTheSame(
            oldItem: NotificationEntity,
            newItem: NotificationEntity
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: NotificationEntity,
            newItem: NotificationEntity
        ): Boolean {
            return oldItem == newItem
        }
    }
}


