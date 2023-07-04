package com.example.photos.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.photos.base.data.transaction.unfold
import com.example.photos.domain.usecase.ExampleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
     useCase: ExampleUseCase
) : ViewModel() {

    val state = useCase.invoke().unfold(
        loading = {ExampleState.LoadingState},
        success = {ExampleState.Content(it.title)},
        failure = { _, errorStatus -> ExampleState.ErrorState(errorStatus)}
    ).stateIn(
        scope = viewModelScope,
        initialValue = ExampleState.LoadingState,
        started = SharingStarted.WhileSubscribed()
    )
}