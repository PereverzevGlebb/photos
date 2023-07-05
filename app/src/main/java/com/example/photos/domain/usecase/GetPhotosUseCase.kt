package com.example.photos.domain.usecase

import com.example.photos.di.IODispatcher
import com.example.photos.repository.unsplash.UnsplashRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetPhotosUseCase @Inject constructor(
    private val unsplashRepository: UnsplashRepository,
    @IODispatcher
    private val dispatcher: CoroutineDispatcher
) {
    operator fun invoke() =
        unsplashRepository.getPhotos().flowOn(dispatcher)
}