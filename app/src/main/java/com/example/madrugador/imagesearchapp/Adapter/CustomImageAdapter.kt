package com.example.madrugador.imagesearchapp.Adapter

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.squareup.picasso.Picasso
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.LinearLayout
import com.example.madrugador.imagesearchapp.model.ImgurItem
import com.example.madrugador.imagesearchapp.Activity.ImgDisplayActivity
import com.example.madrugador.imagesearchapp.R

class CustomImageAdapter(private var images: List<ImgurItem>?, private var rowLayout: Int)
    : RecyclerView.Adapter<CustomImageAdapter.ImageViewHolder>() {

    //A view holder inner class where we get reference to the views in the layout using their ID
    class ImageViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var imagesLayout: LinearLayout? = null
        internal var imageTitle: TextView? = null
        internal var imageLink: TextView? = null
        internal var image: ImageView? = null

        init {
            imagesLayout = v.findViewById(R.id.imgListLayout)
            image = v.findViewById(R.id.image_item)
            imageTitle = v.findViewById(R.id.image_title)
            imageLink = v.findViewById(R.id.image_link_text)

            image?.setOnClickListener() {
                Log.d(CustomImageAdapter::class.java.simpleName, "ImageView Clicked")

                val intent = Intent(v.context, ImgDisplayActivity::class.java)
                intent.putExtra("imageLinkText", imageLink?.text.toString())
                intent.putExtra("imageTitleText", imageTitle?.text.toString())

                v.context.startActivity(intent)
            }
        }
    }

    fun getImages(): List<ImgurItem>? {
        return this.images
    }

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): CustomImageAdapter.ImageViewHolder {

            val view = LayoutInflater.from(parent.context).inflate(rowLayout, parent, false)
            return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val isAlbum = images?.get(position)?.is_album

        if (isAlbum != null && isAlbum == true)
        {
            val imageData = images?.get(position)?.images?.get(0)
            val imageUrl = imageData?.link

            Picasso.with(holder.itemView.context)
                    .load(imageUrl)
                    .placeholder(android.R.drawable.sym_def_app_icon)
                    .error(android.R.drawable.sym_def_app_icon)
                    .into(holder.image)

            holder.imageLink?.text = imageData?.link
        }
        else {
            val imageUrl = images?.get(position)?.link
            Picasso.with(holder.itemView.context)
                    .load(imageUrl)
                    .placeholder(android.R.drawable.sym_def_app_icon)
                    .error(android.R.drawable.sym_def_app_icon)
                    .into(holder.image)
            holder.imageLink?.text = images?.get(position)?.link
        }

        holder.imageTitle?.text = images?.get(position)?.title
    }

    override fun getItemCount(): Int {
        var imageCount = 0

        if (images != null)
        {
            imageCount = images!!.size
        }

        return imageCount
    }

    companion object {
        const val IMAGE_URL_BASE_PATH = "https://api.imgur.com/"
    }
}