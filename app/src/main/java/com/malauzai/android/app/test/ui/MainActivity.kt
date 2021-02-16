package com.malauzai.android.app.test.ui


import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.malauzai.android.app.test.R
import com.malauzai.android.app.test.model.PhotosItem
import com.malauzai.android.app.test.ui.adapter.SearchListAdapter
import com.malauzai.android.app.test.utils.NetworkHelper
import com.malauzai.android.app.test.utils.Status
import com.malauzai.android.app.test.viewmodels.SearchViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var searchViewModel: SearchViewModel
    private lateinit var searchListAdapter: SearchListAdapter
    private lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        searchlist.layoutManager = layoutManager

        //Instantiating viewmodel
        searchViewModel = ViewModelProvider(this).get(SearchViewModel::class.java)

        retry.setOnClickListener(this)

        //Observing data from viewmodel
        searchViewModel.getMarsPhotsList().observe(
            this, Observer {
                when (it.status) {
                    Status.LOADING -> showLoading()
                    Status.SUCCESS -> it.data?.let { it -> displayList(it) }
                }
            }
        )

        //observing error
        searchViewModel.getError().observe(this, Observer {
            it.data?.let { message ->
                displayError(message)
            }
        })


        refreshLayout.setOnRefreshListener {
            retrySearch()
        }
    }

    //displays errors with the given string and hide recyclerview
    private fun displayError(message: String) {
        error_message.text = message
        error_message.visibility = View.VISIBLE
        retry.visibility = View.VISIBLE
        searchlist.visibility = View.GONE
        hideLoading()
    }

    //displays loading progressbar
    private fun showLoading() {
        //loading.visibility = View.VISIBLE
        refreshLayout.isRefreshing = true
        error_message.visibility = View.GONE
        retry.visibility = View.GONE
    }

    //hide loading progressbar
    private fun hideLoading() {
        //loading.visibility = View.GONE
        refreshLayout.isRefreshing = false
    }

    //displays search list
    private fun displayList(list: List<PhotosItem>) {
        hideLoading()
        error_message.visibility = View.GONE
        retry.visibility = View.GONE
        searchlist.visibility = View.VISIBLE
        if (list.isEmpty()) {
            displayError(getString(R.string.no_results_found))
        }
        //setting data to adapter
        searchListAdapter = SearchListAdapter(this, list)
        searchlist.adapter = searchListAdapter
    }

    override fun onResume() {
        super.onResume()

        //If no network displaying error
        if (!NetworkHelper.getInstance().isNetworkConnected(this)) {
            searchViewModel.setError()
        } else {
            //Fetching data: App is in background navigating back to app
            var status = searchViewModel?.getError().value?.status
            if (status == Status.NETWORKERROR || status == Status.ERROR) {
                //fetching mars photos
                searchViewModel.getMarsPhotos()
            }
        }
    }


    // Retry button functionality
    private fun retrySearch() {
        searchViewModel.getMarsPhotos()
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.retry -> {
                retrySearch()
            }
        }
    }
}
