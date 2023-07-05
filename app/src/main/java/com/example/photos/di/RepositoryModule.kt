package com.example.photos.di

import com.example.photos.repository.unsplash.PhotosRepository
import com.example.photos.repository.unsplash.PhotosRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    fun bindsExampleRepository(impl: PhotosRepositoryImpl): PhotosRepository
}