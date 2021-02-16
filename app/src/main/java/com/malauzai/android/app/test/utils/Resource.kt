package com.malauzai.android.app.test.utils

//Helps in handling different type network handling use cases
data class Resource<out T> private constructor(val status: Status, val data: T?) {

    companion object {
        fun <T> success(data: T? = null): Resource<T> = Resource(Status.SUCCESS, data)

        //webservice error bad response
        //fun <T> error(data: T? = null): Resource<List<ResultItem>>? = Resource(Status.ERROR, data)

        fun <T> loading(data: T? = null): Resource<T> = Resource(Status.LOADING, data)

        //no network error
        fun <T> networkError(data: T? = null): Resource<T> = Resource(Status.NETWORKERROR, data)


    }
}