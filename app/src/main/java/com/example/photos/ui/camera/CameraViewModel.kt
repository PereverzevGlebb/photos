package com.example.photos.ui.camera

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.photos.domain.usecase.SavePhotoToGalleryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(
    private val savePhotoToGalleryUseCase: SavePhotoToGalleryUseCase
) : ViewModel() {

    interface Event {
        data class OnSaveSuccess(val uri: String): Event
        object OnSaveError: Event
    }

    private val _event = MutableSharedFlow<Event>()

    val event: Flow<Event>
        get() = _event

    fun savePhotoToGallery(bitmap: Bitmap) {
        viewModelScope.launch {
           val uri = savePhotoToGalleryUseCase(bitmap)
            if(uri != null)
                _event.emit(Event.OnSaveSuccess(uri))
            else
                _event.emit(Event.OnSaveError)
        }
    }
}