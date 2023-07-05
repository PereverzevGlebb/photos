package com.example.photos.ui.edit_photo

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.photos.domain.usecase.SavePhotoToGalleryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditPhotoViewModel @Inject constructor(
    private val savePhotoToGalleryUseCase: SavePhotoToGalleryUseCase
): ViewModel() {

    fun savePhotoToGallery(bitmap: Bitmap) {
        viewModelScope.launch {
            savePhotoToGalleryUseCase(bitmap)
        }
    }
}