package com.example.nasapictures.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.nasapictures.R
import com.example.nasapictures.extensions.loadFromUrl
import com.example.nasapictures.model.ImageDetailsModel
import com.example.nasapictures.view.component.ImageCarouselZoomLayout

class ImageCarouselAdapter (
    private val context: Context,
    private val dataSet: List<ImageDetailsModel>,
    private val zoomLayoutOnClick:(() -> Unit)
) :
    RecyclerView.Adapter<ImageCarouselAdapter.ImageCarouselViewHolder>() {
    private var canScroll = true

    override fun getItemCount(): Int {
        return dataSet.size
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ImageCarouselViewHolder {
        val item = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_image_carousel_item, parent, false) as RelativeLayout

        return ImageCarouselViewHolder(item)
    }


    fun getCanScroll(): Boolean {
        return canScroll
    }

    override fun onBindViewHolder(holder: ImageCarouselViewHolder, position: Int) {
        holder.bind()
    }


    inner class ImageCarouselViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val imageView: ImageView = itemView.findViewById(R.id.image_carousel_zoom_image)
        private val zoomLayout: ImageCarouselZoomLayout = itemView.findViewById(R.id.zoom_layout)
        private val loader: ProgressBar = itemView.findViewById(R.id.image_carousel_loader)
        private val imageDetails: ImageDetailsModel?
            get() {
                return dataSet.getOrNull(adapterPosition)
            }
        init {
            zoomLayout.assignZoomClickListener(zoomLayoutOnClick)
            zoomLayout.assignScaleChangeListener {
                canScroll = it
            }
        }

        fun bind() {
            itemView.scaleX = 1f
            itemView.scaleY = 1f
            loader.visibility = View.VISIBLE
            imageView.loadFromUrl(imageDetails?.hdurl, imageLoadFailure = this::onImageFailure,
                imageLoadSuccessful = this::onImageLoadSuccess)
        }

        private fun onImageFailure() {
            Toast.makeText(context, "Image load failed", Toast.LENGTH_SHORT).show()
            loader.visibility = View.GONE
        }

        private fun onImageLoadSuccess() {
            loader.visibility = View.GONE
        }
    }
}


