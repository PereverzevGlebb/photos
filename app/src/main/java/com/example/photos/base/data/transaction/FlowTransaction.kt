package com.example.photos.base.data.transaction

import com.example.photos.base.data.exceptions.fromException
import com.example.photos.base.data.transaction.entity.Transaction
import com.example.photos.base.data.transaction.entity.TransactionStatus
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

typealias FlowTransaction<T> = Flow<Transaction<T>>


fun <T> Transaction.Companion.flowWrap(
    block: suspend () -> T
): FlowTransaction<T> = flow {
    emit(Transaction.Loading)
    emit(Transaction.Success(block(), TransactionStatus.SUCCESS))
}.catch { exception ->
    emit(Transaction.Failure(exception.fromException(), exception))
}

fun <T> Flow<T?>.wrap(): FlowTransaction<T> = flow {
    emit(Transaction.Loading)
    emit(Transaction.Success(filterNotNull().first(), TransactionStatus.SUCCESS))
}.catch { exception ->
    emit(Transaction.Failure(exception.fromException(), exception))
}

fun <IN, OUT> FlowTransaction<IN>.transform(
    transformer: suspend (IN) -> OUT
): FlowTransaction<OUT> = map { result ->
    when (result) {
        is Transaction.Failure -> result
        is Transaction.Loading -> result
        is Transaction.Success -> Transaction.Success(transformer(result.data), result.status)
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
fun <IN, OUT> FlowTransaction<IN>.flatTransform(
    transformer: suspend (IN) -> FlowTransaction<OUT>
): FlowTransaction<OUT> = transformLatest { result ->
    when (result) {
        is Transaction.Failure -> emit(result)
        is Transaction.Loading -> emit(result)
        is Transaction.Success -> emitAll(transformer(result.data))
    }
}

fun <IN, OUT> FlowTransaction<IN>.unfold(
    loading: suspend () -> OUT,
    success: suspend (data: IN) -> OUT,
    failure: suspend (cause: Throwable?, status: TransactionStatus) -> OUT
): Flow<OUT> = map { result ->
    when (result) {
        is Transaction.Failure -> failure(result.cause, result.status)
        is Transaction.Loading -> loading()
        is Transaction.Success -> success(result.data)
    }
}.catch { exception ->
    emit(failure(exception, exception.fromException()))
}