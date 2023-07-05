package com.example.photos.di

import com.example.photos.datasource.local.ContentResolverDataSource
import com.example.photos.datasource.local.ContentResolverDataSourceImpl
import com.example.photos.datasource.remote.UnsplashRemoteDataSource
import com.example.photos.datasource.remote.UnsplashRemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DataSourceModule {

    @Binds
    fun bindsExampleDataSource(impl: UnsplashRemoteDataSourceImpl): UnsplashRemoteDataSource

    @Binds
    fun bindsContentResolverDataSource(impl: ContentResolverDataSourceImpl): ContentResolverDataSource
}