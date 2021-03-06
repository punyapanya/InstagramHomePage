package com.example.instagrampage.util

data class Result<out T>(
    val status: Status,
    val data: T?,
    val message: String?
) {

  enum class Status {
    LOADING,
    SUCCESS,
    ERROR
  }

  companion object {
    fun <T> success(data: T) = Result<T>(Status.SUCCESS, data, null)
    fun <T> error(message: String) = Result<T>(Status.ERROR, null, message)
    fun <T> loading() = Result<T>(Status.LOADING, null, null)
  }
}
