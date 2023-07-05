package com.example.photos.repository.unsplash

import android.graphics.Bitmap
import com.example.photos.base.data.transaction.FlowTransaction
import com.example.photos.domain.entity.PhotoModel

interface PhotosRepository {
    fun getPhotos(): FlowTransaction<List<PhotoModel>>
    fun getPhotoFromDirectory(): FlowTransaction<List<PhotoModel>>
    fun savePhotoToGallery(bitmap: Bitmap): String?
}