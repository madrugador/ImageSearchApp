package com.example.madrugador.imagesearchapp.Rest

import com.example.madrugador.imagesearchapp.model.ImgurResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


/**
 * Created by wiggl on 3/17/2018.
 */
interface ImgurApiService {
    @GET("3/gallery/search/time/{page}")
    fun getImgureImages(@Path("page") pageNumber: Int,
                        @Query("q") searchQuery: String?)
            : Call<ImgurResponse>?
}