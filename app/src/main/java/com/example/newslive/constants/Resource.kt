package com.example.newslive.constants

//sealed class Resource<T> (
//    val data: T? = null,
//    val message: String? = null
//) {
//    class Success<T>(data: T) : Resource<T>(data)
//    class Error<T>(message: String, data: T? = null ) : Resource<T>(data,message)
//    class Loading<T> : Resource<T>()
//}

sealed class Resource<out T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<out T>(data: T) : Resource<T>(data = data)
    class Error<out T>(message: String, data: T? = null) : Resource<T>(data = data, message = message)
    class Loading<out T> : Resource<T>()
}
