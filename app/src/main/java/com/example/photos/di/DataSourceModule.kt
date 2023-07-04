package com.example.photos.di

import com.example.photos.datasource.remote.ExampleRemoteDataSource
import com.example.photos.datasource.remote.ExampleRemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DataSourceModule {

    @Binds
    fun bindsExampleDataSource(impl: ExampleRemoteDataSourceImpl): ExampleRemoteDataSource
}