package com.example.madrugador.imagesearchapp.model

import java.io.Serializable

/**
 * Created by wiggl on 3/17/2018.
 */
data class ImgurVote (
        val ups: Int = 0,
        val downs: Int = 0
) : Serializable