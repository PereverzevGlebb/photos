package com.example.photos.ui.photo_feed

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.photos.R
import com.example.photos.databinding.PhotoItemBinding
import com.example.photos.entity.PhotoItem

class PhotoFeedAdapter(
    //private val onPhotoClick: (PhotoItem) -> Unit
) : ListAdapter<PhotoItem, PhotoFeedAdapter.ViewHolder>(DiffUtilsCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(PhotoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(
        private val binding: PhotoItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: PhotoItem) = with(binding) {
            binding.photoItemImv.setImageResource(R.drawable.test_img)
            binding.root.setOnClickListener {
                //onPhotoClick(item)
                Toast.makeText(binding.root.context, "Will be opened", Toast.LENGTH_SHORT).show()
            }
        }
    }

    object DiffUtilsCallback : DiffUtil.ItemCallback<PhotoItem>() {
        override fun areItemsTheSame(
            oldItem: PhotoItem,
            newItem: PhotoItem
        ) = oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: PhotoItem,
            newItem: PhotoItem
        ) = oldItem == newItem


    }
}