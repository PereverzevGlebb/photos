package com.example.photos.repository.unsplash

import com.example.photos.base.data.transaction.FlowTransaction
import com.example.photos.datasource.remote.UnsplashRemoteDataSource
import com.example.photos.domain.entity.PhotoModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UnsplashRepositoryImpl @Inject constructor(
    private val remoteDataSource: UnsplashRemoteDataSource
) : UnsplashRepository {
    override fun getPhotos(): FlowTransaction<List<PhotoModel>> = remoteDataSource.getPhotos()
}