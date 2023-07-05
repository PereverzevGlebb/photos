package com.example.photos.repository.unsplash

import com.example.photos.base.data.transaction.FlowTransaction
import com.example.photos.domain.entity.PhotoModel

interface UnsplashRepository {
    fun getPhotos(): FlowTransaction<List<PhotoModel>>
}