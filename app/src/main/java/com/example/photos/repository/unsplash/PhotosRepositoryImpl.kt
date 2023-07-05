package com.example.photos.repository.unsplash

import android.graphics.Bitmap
import com.example.photos.base.data.transaction.FlowTransaction
import com.example.photos.datasource.local.ContentResolverDataSource
import com.example.photos.datasource.remote.UnsplashRemoteDataSource
import com.example.photos.domain.entity.PhotoModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PhotosRepositoryImpl @Inject constructor(
    private val remoteDataSource: UnsplashRemoteDataSource,
    private val contentResolverDataSource: ContentResolverDataSource
) : PhotosRepository {
    override fun getPhotos(): FlowTransaction<List<PhotoModel>> = remoteDataSource.getPhotos()

    override fun getPhotoFromDirectory(): FlowTransaction<List<PhotoModel>> = contentResolverDataSource.getPhotosFromDirectory()

    override fun savePhotoToGallery(bitmap: Bitmap) = contentResolverDataSource.savePhotoToGallery(bitmap)

}