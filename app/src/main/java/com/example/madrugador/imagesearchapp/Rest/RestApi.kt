package com.example.madrugador.imagesearchapp.Rest

import com.example.madrugador.imagesearchapp.Adapter.CustomImageAdapter
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by wiggl on 3/17/2018.
 */
class RestApi {
    companion object {
        fun GetImgurApiService(retrofitRef: Retrofit?): ImgurApiService? {
            return retrofitRef?.create(ImgurApiService::class.java)
        }

        fun GetRetroFitRef() : Retrofit {
            val okHttpClient = OkHttpClient.Builder()
                    .authenticator { route, response ->
                        response.request().newBuilder()
                                .header("Authorization", "Client-ID 126701cd8332f32")
                                .build()
                    }.build()

                val retrofit = Retrofit.Builder()
                        .baseUrl(CustomImageAdapter.IMAGE_URL_BASE_PATH)
                        .client(okHttpClient)
                        .addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory.create())
                        .build();

            return retrofit
        }
    }
}