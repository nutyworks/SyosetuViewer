package me.nutyworks.syosetuviewerv2.utilities

sealed class Result<T> {
    class Success<T>(val data: T) : Result<T>()
    class Failure<T>(val throwable: Throwable) : Result<T>()
}
