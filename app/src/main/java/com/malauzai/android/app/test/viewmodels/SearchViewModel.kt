package com.malauzai.android.app.test.viewmodels


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.malauzai.android.app.test.R
import com.malauzai.android.app.test.model.PhotosItem
import com.malauzai.android.app.test.model.repository.SearchRepository
import com.malauzai.android.app.test.utils.Resource
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import retrofit2.HttpException
import java.net.UnknownHostException

class SearchViewModel(private val appContext: Application) : AndroidViewModel(appContext) {

    private val searchList = MutableLiveData<Resource<List<PhotosItem>>>()
    private val errorMessage = MutableLiveData<Resource<String>>()

    private val compositeDisposable = CompositeDisposable()
    private var disposable: Disposable? = null

    private val searchRepository = SearchRepository.getInstance()

    init {
        getMarsPhotos()
    }

    //requesting Repository for data, hard coded to earth_date for now. We can accept date dynamically but for now minimizing the scope.
    fun getMarsPhotos(earth_date: String = "2015-6-3") {
        disposable?.dispose()
            disposable = searchRepository.getMarsPhotos(earth_date)
                .doOnSubscribe {
                    //helps to show loading progress user
                    searchList.postValue(Resource.loading())
                }
                .subscribe(
                    //on success
                    { searchList.postValue(Resource.success(it)) },
                    //on error
                    { handleError(it) }
                )
            compositeDisposable.add(disposable!!)
    }

    private fun handleError(throwable: Throwable) {
        val errorResource = when (throwable) {
            is HttpException -> R.string.webserive_error
            is UnknownHostException -> R.string.network_error
            else -> R.string.webserive_error
        }
        errorMessage.postValue(Resource.networkError(appContext.getString(errorResource)))
    }

    //emits observable of data to observers (here observer is MainActivity)
    fun getMarsPhotsList(): LiveData<Resource<List<PhotosItem>>> {
        return searchList
    }

    fun setError() = errorMessage.postValue(Resource.networkError(appContext.getString(R.string.network_error)))

    fun getError(): LiveData<Resource<String>> = errorMessage

    //clearing observables
    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }
}