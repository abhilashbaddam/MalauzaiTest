package com.malauzai.android.app.test.model.repository


import com.malauzai.android.app.test.model.PhotosItem
import com.malauzai.android.app.test.networking.NetworkService
import com.malauzai.android.app.test.networking.Networking.getNetworkApi
import com.malauzai.android.app.test.utils.Utility
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class SearchRepository () {

    private val TAG = this::class.java.simpleName
    private var api: NetworkService = getNetworkApi().create(NetworkService::class.java)

    companion object {
        @Volatile
        private var INSTANCE: SearchRepository? = null

        fun getInstance(): SearchRepository =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: SearchRepository().also { INSTANCE = it }
            }
    }

    //Fetching data from server and emits data to observers(here its searchviewmodel and setting default search to earth_date)
    fun getMarsPhotos(earth_date: String = "2015-6-3"): Single<List<PhotosItem>> =
        Single.create { emitter ->
            api.doSearch(earth_date, Utility.apiKey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        emitter.onSuccess(it.photos)
                    },
                    {
                        emitter.onError(it)
                    }
                )
        }
}