package com.example.fakestore

sealed class ResultState<out T> {
    object Idle : ResultState<Nothing>()
    object Processing : ResultState<Nothing>()
    data class Success<out T>(val data: T) : ResultState<T>()
    data class Failure(val message: String, val throwable: Throwable? = null) :
        ResultState<Nothing>()
}
