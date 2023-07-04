package com.example.photos.datasource.remote

import com.example.photos.base.data.transaction.FlowTransaction
import com.example.photos.base.data.transaction.entity.Transaction
import com.example.photos.base.data.transaction.flowWrap
import com.example.photos.base.data.transaction.transform
import com.example.photos.datasource.ExampleApi
import com.example.photos.domain.entity.ExampleModel
import javax.inject.Inject

class ExampleRemoteDataSourceImpl @Inject constructor(
    private val exampleApi: ExampleApi
): ExampleRemoteDataSource {
    override fun getExample(): FlowTransaction<ExampleModel> = Transaction.flowWrap {

        exampleApi.getExample()
    }.transform { example ->

        ExampleModel(
            title = example.example
        )
    }
}