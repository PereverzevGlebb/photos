package com.example.photos.di

import com.example.photos.datasource.UnsplashApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
class ApiModule {

    @Provides
    fun provideExampleApi(retrofit: Retrofit) = retrofit.create(UnsplashApi::class.java)

}