package com.example.assignment2.util

/**
 * A generic wrapper used to represent the state of an operation (e.g. a network call)
 * as it moves through Loading -> Success/Error. This keeps ViewModels and UI code
 * free of try/catch blocks and makes state easy to observe via LiveData.
 */
sealed class Resource<out T> {
    data class Success<out T>(val data: T) : Resource<T>()
    data class Error(val message: String) : Resource<Nothing>()
    object Loading : Resource<Nothing>()
}
