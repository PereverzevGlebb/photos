package com.example.photos.domain.usecase

import com.example.photos.di.IODispatcher
import com.example.photos.repository.example.ExampleRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ExampleUseCase @Inject constructor(
    private val exampleRepository: ExampleRepository,
    @IODispatcher
    private val dispatcher: CoroutineDispatcher
) {
    operator fun invoke() =
        exampleRepository.getExample().flowOn(dispatcher)
}