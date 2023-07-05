package com.example.photos.domain.usecase

import com.example.photos.di.IODispatcher
import com.example.photos.repository.unsplash.PhotosRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetPhotosFromDirectoryUseCase @Inject constructor(
    private val repository: PhotosRepository,
    @IODispatcher
    private val dispatcher: CoroutineDispatcher
) {

    operator fun invoke() = repository.getPhotoFromDirectory().flowOn(dispatcher)
}