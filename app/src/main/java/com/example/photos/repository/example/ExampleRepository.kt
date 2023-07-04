package com.example.photos.repository.example

import com.example.photos.base.data.transaction.FlowTransaction
import com.example.photos.domain.entity.ExampleModel

interface ExampleRepository {

    fun getExample(): FlowTransaction<ExampleModel>

}