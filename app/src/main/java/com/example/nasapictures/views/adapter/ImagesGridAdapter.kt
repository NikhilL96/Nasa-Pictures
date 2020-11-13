package com.example.nasapictures.views.adapter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.nasapictures.R
import com.example.nasapictures.extensions.loadFromUrl
import com.example.nasapictures.model.ImageDetailsModel
import java.io.InputStream
import java.net.URL


class ImagesGridAdapter(
    private val dataSet: List<ImageDetailsModel>,
    private val onImageClick: (position: Int) -> Unit
) :
        RecyclerView.Adapter<ImagesGridAdapter.ImagesGridViewHolder>() {

    override fun getItemCount(): Int {
        return dataSet.size
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ImagesGridViewHolder {
        val item = LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_image_grid_item, parent, false) as CardView

        return ImagesGridViewHolder(item)
    }

    override fun onBindViewHolder(holder: ImagesGridViewHolder, position: Int) {
        holder.bind()
//        holder.setIsRecyclable(false)
    }


    inner class ImagesGridViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val titleTv: TextView = itemView.findViewById(R.id.image_grid_item_title)
        private val image: ImageView = itemView.findViewById(R.id.images_grid_item_image_view)
        private val imageSelectableView: View = itemView.findViewById(R.id.image_clickable_view)
        private val loader: ProgressBar = itemView.findViewById(R.id.images_grid_item_loader)
        private val imageDetails: ImageDetailsModel?
        get() {
            return dataSet.getOrNull(adapterPosition)
        }

        init {
            imageSelectableView.setOnClickListener {
                onImageClick(adapterPosition)
            }
        }

        fun bind() {
            loader.visibility = View.VISIBLE
            titleTv.text = imageDetails?.title
            image.loadFromUrl(
                imageDetails?.url, imageLoadFailure = this::onImageFailure,
                imageLoadSuccessful = this::onImageLoadSuccess
            )
        }

        private fun onImageFailure() {
            loader.visibility = View.GONE
        }

        private fun onImageLoadSuccess() {
            loader.visibility = View.GONE
        }
    }
}


