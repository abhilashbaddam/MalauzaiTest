package com.malauzai.android.app.test.networking

import com.malauzai.android.app.test.model.Response
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface NetworkService {
    @GET("/mars-photos/api/v1/rovers/curiosity/photos")
    fun doSearch(
        @Query("earth_date") date: String?,
        @Query("api_key") api: String
    ): Single<Response>
}