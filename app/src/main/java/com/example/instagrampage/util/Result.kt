package com.example.instagrampage.util

sealed class Result<T> {
    class Loading<T> : Result<T>()
    class Success<T>(val data: T) : Result<T>()
    class Error<T>(val message: String) : Result<T>()
}
