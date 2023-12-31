package com.example.photos.domain.usecase

import com.example.photos.di.IODispatcher
import com.example.photos.repository.unsplash.PhotosRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetPhotosUseCase @Inject constructor(
    private val photosRepository: PhotosRepository,
    @IODispatcher
    private val dispatcher: CoroutineDispatcher
) {
    operator fun invoke() =
        photosRepository.getPhotos().flowOn(dispatcher)
}