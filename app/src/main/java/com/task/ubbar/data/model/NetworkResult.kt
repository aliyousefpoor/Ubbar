package com.task.ubbar.data.model

sealed class NetworkResult<out T> {
    data class Success<T>(val data: T) : NetworkResult<T>()
    data class HttpException<T>(val errorCode: Int, val errorMessage: String?) : NetworkResult<T>()
    data class NetworkError<T>(val exception: Throwable) : NetworkResult<T>()
    data class Loading<T>(val data: T? = null) : NetworkResult<T>()
}