package com.example.photos.datasource.remote

import com.example.photos.base.data.transaction.FlowTransaction
import com.example.photos.domain.entity.PhotoModel

interface UnsplashRemoteDataSource {
    fun getPhotos(): FlowTransaction<List<PhotoModel>>
}