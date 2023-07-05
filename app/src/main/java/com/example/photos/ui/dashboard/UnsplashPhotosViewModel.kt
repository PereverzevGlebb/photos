package com.example.photos.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.photos.base.data.transaction.unfold
import com.example.photos.domain.usecase.GetPhotosUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class UnsplashPhotosViewModel @Inject constructor(
     useCase: GetPhotosUseCase
) : ViewModel() {

     val unsplashState = useCase.invoke().unfold(
          loading = { UnsplashState.Loading},
          success = { UnsplashState.fromContent(it)},
          failure = { e, _ -> e?.printStackTrace()
               UnsplashState.Error}
     ).stateIn(
          scope = viewModelScope,
          initialValue = UnsplashState.Loading,
          started = SharingStarted.WhileSubscribed()
     )
}