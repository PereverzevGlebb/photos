package com.example.photos.domain.usecase

import android.graphics.Bitmap
import com.example.photos.di.IODispatcher
import com.example.photos.repository.unsplash.PhotosRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SavePhotoToGalleryUseCase @Inject constructor(
    private val repository: PhotosRepository,
    @IODispatcher
    private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(bitmap: Bitmap) = withContext(dispatcher) {
        repository.savePhotoToGallery(bitmap)
    }
}