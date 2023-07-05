package com.example.photos.ui.photo_feed

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.photos.base.data.transaction.unfold
import com.example.photos.domain.usecase.GetPhotosFromDirectoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class PhotoFeedViewModel @Inject constructor(
    useCase: GetPhotosFromDirectoryUseCase
) : ViewModel() {

    private val refreshTrigger = MutableStateFlow<Boolean?>(null)

    val photoGalleryState = refreshTrigger
        .filterNotNull()
        .flatMapLatest {
            useCase.invoke().onEach {
                Log.e("CHECK_PHOTOS", it.toString())
            }
        }
        .unfold(
            loading = { PhotoFeedState.Loading },
            success = { PhotoFeedState.fromContent(it) },
            failure = { e, _ ->
                e?.printStackTrace()
                PhotoFeedState.Error
            }
        ).stateIn(
            scope = viewModelScope,
            initialValue = PhotoFeedState.Loading,
            started = SharingStarted.WhileSubscribed()
        )


    fun refresh() {
        refreshTrigger.value = true
    }

}