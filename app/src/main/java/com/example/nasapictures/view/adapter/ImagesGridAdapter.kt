package com.example.nasapictures.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.nasapictures.R
import com.example.nasapictures.extensions.loadFromUrl
import com.example.nasapictures.model.ImageDetailsModel


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
    }


    inner class ImagesGridViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val titleTv: TextView = itemView.findViewById(R.id.image_grid_item_title)
        private val image: ImageView = itemView.findViewById(R.id.images_grid_item_image_view)
        private val loader: ProgressBar = itemView.findViewById(R.id.images_grid_item_loader)
        private val imageDetails: ImageDetailsModel?
        get() {
            return dataSet.getOrNull(adapterPosition)
        }

        init {
            itemView.setOnClickListener {
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


