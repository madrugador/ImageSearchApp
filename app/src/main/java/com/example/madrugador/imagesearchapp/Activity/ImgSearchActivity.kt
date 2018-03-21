package com.example.madrugador.imagesearchapp.Activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_img_search.*
import android.app.SearchManager
import android.content.Context
import android.content.res.Configuration
import android.os.Handler
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.LinearLayoutManager
import com.example.madrugador.imagesearchapp.Adapter.EndlessRecyclerViewScrollListener
import com.example.madrugador.imagesearchapp.R
import com.example.madrugador.imagesearchapp.model.ImgurResponse
import com.example.madrugador.imagesearchapp.Rest.RestApi
import retrofit2.Call
import android.util.Log
import android.view.View
import com.example.madrugador.imagesearchapp.Adapter.CustomImageAdapter
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import android.widget.ProgressBar
import android.view.inputmethod.InputMethodManager
import com.example.madrugador.imagesearchapp.model.ImgurItem
import java.io.Serializable


class ImgSearchActivity : AppCompatActivity(), SearchView.OnQueryTextListener {

    private var mSearchView: SearchView? = null
    private var searchFilter: String? = ""
    private var scrollListener: EndlessRecyclerViewScrollListener? = null
    private var linearLayoutManager: LinearLayoutManager? = null
    private var endlessScrollPageNumber: Int = 0
    private var rvItems: RecyclerView? = null
    private var imageAdapter: CustomImageAdapter? = null
    private val TAG = ImgSearchActivity::class.java.simpleName
    private var retrofit: Retrofit? = null
    private var isLoading = false
    private var progressBar: ProgressBar? = null
    private val handler: Handler? = null
    private var isPortrait: Boolean = true
    private val RECYCLER_VIEW_POSITION: String = "imagesViewPosition"
    private val SEARCH_FILTER_TEXT: String = "searchFilterText"
    private val IMAGE_DATA_LIST: String = "imageDataList"
    private var mImageDstaList: ArrayList<ImgurItem>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_img_search)
        setSupportActionBar(imgSearchToolbar)

        when (resources.configuration.orientation) {
            Configuration.ORIENTATION_PORTRAIT -> isPortrait = true
            Configuration.ORIENTATION_LANDSCAPE -> isPortrait = false
        }

        initializeControls()

        var recyclerViewPosition = 0
        if (savedInstanceState != null) {
            recyclerViewPosition = savedInstanceState.getInt(RECYCLER_VIEW_POSITION)
            searchFilter = savedInstanceState.getString(SEARCH_FILTER_TEXT)

            mSearchView?.post({ mSearchView?.setQuery(searchFilter, true) })
        }
        else
        {
            progressBar?.visibility = View.INVISIBLE
        }

        rvItems?.scrollToPosition(recyclerViewPosition)
    }

    private fun initializeControls() {
        progressBar = findViewById(R.id.progressbar)
        rvItems = findViewById(R.id.imageList)
        rvItems?.setHasFixedSize(true)
        rvItems?.isNestedScrollingEnabled = true
        rvItems?.setHasFixedSize(true)

        setScrollListener()

        // Adds the scroll listener to RecyclerView
        rvItems?.addOnScrollListener(scrollListener)
    }

    private fun setScrollListener() {
        linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager?.orientation = LinearLayoutManager.VERTICAL
        rvItems?.layoutManager = linearLayoutManager

        scrollListener = object : EndlessRecyclerViewScrollListener(linearLayoutManager!!) {
            override fun onLoadMore() {
                Log.d(TAG, "EndlessRecyclerViewScrollListener->onLoadMore")

                isLoading = true

                progressBar?.visibility = View.VISIBLE

                handler?.postDelayed({
                    loadNextPage()

                    imageAdapter?.notifyDataSetChanged()

                    //or you can add all at once but do not forget to call mAdapter.notifyDataSetChanged();
                }, 2000)

                isLoading = false
                progressBar?.visibility = View.GONE
            }

            override fun isLoading() : Boolean {
                return isLoading
            }
        }
    }

    public override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)

        //save the current recyclerview position
        rvItems = findViewById(R.id.imageList)
        outState!!.putInt(RECYCLER_VIEW_POSITION, rvItems?.verticalScrollbarPosition!!)

        outState.putString(SEARCH_FILTER_TEXT, searchFilter)

        mImageDstaList = imageAdapter?.getImages() as ArrayList<ImgurItem>
        outState.putSerializable(IMAGE_DATA_LIST, mImageDstaList as Serializable)
    }

    override fun onRestoreInstanceState(state: Bundle?) {
        super.onRestoreInstanceState(state)

        // Retrieve list state and list/item positions
        if (state != null) {
            mImageDstaList = state.getSerializable(IMAGE_DATA_LIST) as ArrayList<ImgurItem>
        }
    }

    override fun onResume() {
        super.onResume()

        //dismiss soft keyboard
        rvItems = findViewById(R.id.imageList)
        rvItems?.requestFocus();
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(this.currentFocus!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)

        when (resources.configuration.orientation) {
            Configuration.ORIENTATION_PORTRAIT -> isPortrait = true
            Configuration.ORIENTATION_LANDSCAPE -> isPortrait = false
        }

        populateImageAdapter(mImageDstaList)
    }

    private fun loadNextPage() {
        Log.d(TAG, "loadNextPage:$endlessScrollPageNumber")

        loadNextDataFromApi(endlessScrollPageNumber)
        endlessScrollPageNumber++
    }

    private fun loadNextDataFromApi(offset: Int) {
        // Send an API request to retrieve appropriate paginated data
        //  --> Send the request including an offset value (i.e `page`) as a query parameter.
        //  --> Deserialize and construct new model objects from the API response
        //  --> Append the new data objects to the existing set of items inside the array of items
        //  --> Notify the adapter of the new items made with `notifyItemRangeInserted()`

        this.getImagesData(offset, searchFilter)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_scrolling, menu)
        val inflater = menuInflater
        inflater.inflate(R.menu.searchview_in_menu, menu)
        val searchItem = menu.findItem(R.id.action_search)
        mSearchView = searchItem.actionView as SearchView
        setupSearchView(searchItem)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return when (item.itemId) {
            R.id.action_settings ->
                return true
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupSearchView(searchItem: MenuItem) {

        if (isAlwaysExpanded()) {
            mSearchView?.setIconifiedByDefault(false)
        } else {
            searchItem.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM or MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW)
        }

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager

        val searchables = searchManager.searchablesInGlobalSearch

        var info = searchManager.getSearchableInfo(componentName)
        searchables
                .filter { it.suggestAuthority != null && it.suggestAuthority.startsWith("applications") }
                .forEach { info = it }

        mSearchView?.setSearchableInfo(info)

        mSearchView?.setOnQueryTextListener(this)
    }

    override fun onQueryTextChange(newText: String): Boolean {
        return false
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        searchFilter = query
        scrollListener?.resetState()
        endlessScrollPageNumber = 0
        loadNextDataFromApi(endlessScrollPageNumber)
        endlessScrollPageNumber++

        mSearchView?.clearFocus()
        return true
    }

    private fun isAlwaysExpanded(): Boolean {
        return false
    }

    private fun getImagesData(pageNumber: Int, searchFilter: String?) {
        Log.d(TAG, "getImagesData:$pageNumber")

        if (retrofit == null)
        {
            retrofit = RestApi.GetRetroFitRef()
        }

        val imgurService = RestApi.GetImgurApiService(retrofit)

        val apiCall: Call<ImgurResponse>? = imgurService?.getImgureImages(pageNumber, searchFilter)

        apiCall?.enqueue(object : Callback<ImgurResponse> {
            override fun onResponse(call: Call<ImgurResponse>, response: Response<ImgurResponse>) {
                val images = response.body()?.dataResults
                populateImageAdapter(images)
            }

            override fun onFailure(call: Call<ImgurResponse>, throwable: Throwable) {
                Log.e(TAG, throwable.toString())
            }
        })
    }

    private fun populateImageAdapter(imageData: List<ImgurItem>?) {
        val imageAdapterLayout: Int = if (isPortrait) {
            R.layout.content_img_search_portrait
        } else{
            R.layout.content_img_search_landscape
        }

        imageAdapter = CustomImageAdapter(imageData, imageAdapterLayout)
        rvItems?.adapter = imageAdapter
        rvItems?.adapter?.notifyDataSetChanged()

        Log.d(TAG, "Number of images found: " + imageData?.size)
    }
}
