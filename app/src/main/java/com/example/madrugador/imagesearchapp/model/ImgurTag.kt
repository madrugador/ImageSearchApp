package com.example.madrugador.imagesearchapp.model

import java.io.Serializable

/**
 * Created by wiggl on 3/17/2018.
 */
data class ImgurTag (
        val name: String = "",
        val display_name: String = "",
        val followers: Int = 0,
        val total_items: Int = 0,
        val following: Boolean = false,
        val background_hash: String = "",
        val thumbnail_hash: String = "",
        val accent: String = "",
        val backround_is_animated: Boolean = false,
        val is_promoted: Boolean = false,
        val description: String = "",
        val logo_hash: String = "",
        val logo_destination_hash: String = ""
) : Serializable