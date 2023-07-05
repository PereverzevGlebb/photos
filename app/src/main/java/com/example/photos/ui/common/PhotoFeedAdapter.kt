package com.example.photos.ui.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.photos.R
import com.example.photos.databinding.PhotoItemBinding
import com.example.photos.domain.entity.PhotoModel

class PhotoFeedAdapter(
    private val onPhotoClick: (PhotoModel) -> Unit,
    private val onShareClick: (PhotoModel) -> Unit
) : ListAdapter<PhotoModel, PhotoFeedAdapter.ViewHolder>(DiffUtilsCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(PhotoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(
        private val binding: PhotoItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: PhotoModel) = with(binding) {
            Glide.with(photoItemImv)
                .load(item.urls)
                .centerCrop()
                .placeholder(R.drawable.ic_add_photo)
                .into(photoItemImv)

            root.setOnClickListener {
                onPhotoClick(item)
            }
            btnShare.setOnClickListener {
                onShareClick(item)
            }
        }
    }

    object DiffUtilsCallback : DiffUtil.ItemCallback<PhotoModel>() {
        override fun areItemsTheSame(
            oldItem: PhotoModel,
            newItem: PhotoModel
        ) = oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: PhotoModel,
            newItem: PhotoModel
        ) = oldItem == newItem
    }
}