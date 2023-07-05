package com.example.photos.datasource.local

import android.graphics.Bitmap
import com.example.photos.base.data.transaction.FlowTransaction
import com.example.photos.domain.entity.PhotoModel

interface ContentResolverDataSource {

    fun getPhotosFromDirectory(): FlowTransaction<List<PhotoModel>>

    fun savePhotoToGallery(bitmap: Bitmap) : String?
}