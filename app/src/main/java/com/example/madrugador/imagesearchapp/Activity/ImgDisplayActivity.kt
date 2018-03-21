package com.example.madrugador.imagesearchapp.Activity

import android.os.Bundle
import android.util.Log
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.widget.ImageView
import android.widget.TextView
import com.example.madrugador.imagesearchapp.R
import com.squareup.picasso.Picasso


/**
 * Created by wiggl on 3/17/2018.
 */
class ImgDisplayActivity : AppCompatActivity() {

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.display_img_layout)

        val imageLinkText = intent.getStringExtra(INTENT_IMAGE_LINK)
                ?: throw IllegalStateException("field $INTENT_IMAGE_LINK missing in Intent")

        val imageTitleText = intent.getStringExtra(INTENT_IMAGE_TITLE)
                ?: throw IllegalStateException("field $INTENT_IMAGE_TITLE missing in Intent")

        Log.d(ImgDisplayActivity::class.java!!.simpleName, "Image Title: $imageTitleText")
        val toolbarTitle: TextView? = findViewById(R.id.toolbar_title)
        toolbarTitle?.text = imageTitleText

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val imageToDisplay: ImageView = findViewById(R.id.image_display_item)

        Picasso.with(this)
                .load(imageLinkText)
                .placeholder(android.R.drawable.sym_def_app_icon)
                .error(android.R.drawable.sym_def_app_icon)
                .into(imageToDisplay)
    }

    companion object {
        private const val INTENT_IMAGE_LINK = "imageLinkText"
        private const val INTENT_IMAGE_TITLE = "imageTitleText"
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}