package com.example.photos.repository.example

import com.example.photos.base.data.transaction.FlowTransaction
import com.example.photos.datasource.remote.ExampleRemoteDataSource
import com.example.photos.domain.entity.ExampleModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExampleRepositoryImpl @Inject constructor(
    private val remoteDataSource: ExampleRemoteDataSource
) : ExampleRepository {
    override fun getExample(): FlowTransaction<ExampleModel> = remoteDataSource.getExample()
}