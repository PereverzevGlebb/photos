package com.example.photos.ui.dashboard

import com.example.photos.base.data.transaction.entity.TransactionStatus

sealed class ExampleState {

    object LoadingState: ExampleState()

    data class Content(
        val data: String
    ): ExampleState()

    data class ErrorState(
        val error: TransactionStatus
    ): ExampleState()

}