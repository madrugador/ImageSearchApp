package com.example.madrugador.imagesearchapp.model

import java.io.Serializable

/**
 * Created by wiggl on 3/10/2018.
 */
data class ImgurItem (
        var id: String = "",
        var title: String = "",
        val description: String = "",
        val datetime: String = "",
        val cover: String = "",
        val cover_width: Int = 0,
        val cover_height: Int = 0,
        val type: String = "",
        val animated: Boolean = false,
        val width: Int = 0,
        val height: Int = 0,
        val size: Int = 0,
        val views: String = "",
        val bandwidth: Long = 0,
        val vote: ImgurVote? = null,
        val nsfw: Boolean = false,
        val section: String = "",
        val account_url: String = "",
        val account_id: String = "",
        val privacy: String = "",
        val layout: String = "",
        val link: String = "",
        val ups: Int = 0,
        val downs: Int = 0,
        val points: Int = 0,
        val score: Int = 0,
        val is_album: Boolean = false,
        val favorite: Boolean = false,
        val comment_count: Int = 0,
        val favorite_count: Int = 0,
        val topic: String = "",
        val topic_id: Int = 0,
        val images_count: Int = 0,
        val in_gallery: Boolean = false,
        val is_ad: Boolean = false,
        val tags: List<ImgurTag>? = listOf(),
        val ad_typ: Int = 0,
        val ad_url: String = "",
        val in_most_viral: Boolean = false,
        val has_sound: Boolean = false,
        val images: List<ImgurItem>? = listOf()
) : Serializable