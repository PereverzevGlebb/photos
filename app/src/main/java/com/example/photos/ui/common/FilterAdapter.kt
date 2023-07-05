package com.example.photos.ui.common

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.photos.R
import com.example.photos.databinding.FilterItemBinding
import com.example.photos.ui.edit_photo.FilterMatrix
import com.example.photos.utils.applyFilter

class FilterAdapter(
    private val onFilterClick: (FilterMatrix) -> Unit
) : RecyclerView.Adapter<FilterAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(FilterItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(FilterMatrix.values()[position])
    }

    override fun getItemCount() = FilterMatrix.values().size

    inner class ViewHolder(
        private val binding: FilterItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: FilterMatrix) = with(binding) {
            filterTitle.text = item.title
            val filterBitmap =
                BitmapFactory.decodeResource(itemView.resources, R.drawable.ic_new_filter_preview)
                    .applyFilter(item.matrix)
            filterPreview.setImageBitmap(filterBitmap)
            root.setOnClickListener {
                onFilterClick(item)
            }
        }
    }
}