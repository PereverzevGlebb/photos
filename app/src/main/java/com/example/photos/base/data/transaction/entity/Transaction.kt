package com.example.photos.base.data.transaction.entity

sealed class Transaction<out T> {

    data class Success<out T>(
        val data: T,
        val status: TransactionStatus
    ) : Transaction<T>()

    data class Failure(
        val status: TransactionStatus,
        val cause: Throwable?
    ) : Transaction<Nothing>()

    object Loading : Transaction<Nothing>()

    companion object

}