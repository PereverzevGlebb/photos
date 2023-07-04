package com.example.photos.base.data.transaction.entity

enum class TransactionStatus {
    SUCCESS,
    BAD_REQUEST,
    SERVER_ERROR,
    TO_MANY_ATTEMPT,
    NOT_FOUND,
    UNAUTHORIZED,
    TIMEOUT,
    CANCELED,
    NETWORK_ERROR,
    GENERAL_ERROR
}