package com.example.madrugador.imagesearchapp.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Created by wiggl on 3/17/2018.
 */
data class ImgurResponse (
        @SerializedName("data")
        val dataResults: List<ImgurItem>? = listOf()
) : Serializable