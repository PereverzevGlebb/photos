package com.example.photos.datasource.remote

import com.example.photos.base.data.transaction.FlowTransaction
import com.example.photos.base.data.transaction.entity.Transaction
import com.example.photos.base.data.transaction.flowWrap
import com.example.photos.base.data.transaction.transform
import com.example.photos.datasource.UnsplashApi
import com.example.photos.datasource.remote_entity.mapToModel
import com.example.photos.domain.entity.PhotoModel
import javax.inject.Inject

class UnsplashRemoteDataSourceImpl @Inject constructor(
    private val unsplashApi: UnsplashApi
) : UnsplashRemoteDataSource {

    override fun getPhotos(): FlowTransaction<List<PhotoModel>> = Transaction.flowWrap {
        unsplashApi.getPhotos()
    }.transform { list ->
        list.map { it.mapToModel() }
    }
}