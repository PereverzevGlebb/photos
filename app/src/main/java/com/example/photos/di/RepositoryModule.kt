package com.example.photos.di

import com.example.photos.repository.example.ExampleRepository
import com.example.photos.repository.example.ExampleRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    fun bindsExampleRepository(impl: ExampleRepositoryImpl): ExampleRepository
}