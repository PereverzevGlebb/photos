package com.example.photos.base.data.exceptions

import com.example.photos.base.data.transaction.entity.TransactionStatus
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.CancellationException

fun Throwable.fromException(): TransactionStatus {
    return when (this) {
        is HttpException -> fromHttpException()
        is SocketTimeoutException -> TransactionStatus.TIMEOUT
        is CancellationException -> TransactionStatus.CANCELED
        is UnknownHostException -> TransactionStatus.NETWORK_ERROR
        else -> TransactionStatus.GENERAL_ERROR
    }
}

fun HttpException.fromHttpException(): TransactionStatus {
    return when (code()) {
        HttpCode.NOT_FOUND -> TransactionStatus.NOT_FOUND
        HttpCode.UNAUTHORIZED,
        HttpCode.FORBIDDEN -> TransactionStatus.UNAUTHORIZED
        HttpCode.TO_MANY_ATTEMPT -> TransactionStatus.TO_MANY_ATTEMPT
        HttpCode.SERVER_ERROR -> TransactionStatus.SERVER_ERROR
        else -> TransactionStatus.BAD_REQUEST
    }
}