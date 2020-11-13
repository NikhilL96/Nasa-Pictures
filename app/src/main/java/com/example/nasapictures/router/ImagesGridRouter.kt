package com.example.nasapictures.router

import android.app.Activity
import android.content.Intent
import com.example.nasapictures.model.ImageDetailsModel
import com.example.nasapictures.views.activity.ImageDetailsActivity
import java.lang.ref.WeakReference

interface ImagesGridRouterInterface{
    fun passDataToImagesDetails(selectedPosition:Int, imagesList: List<ImageDetailsModel>?)
}

class ImagesGridRouter(private val activity: WeakReference<Activity>): ImagesGridRouterInterface {


    override fun passDataToImagesDetails(selectedPosition:Int, imagesList: List<ImageDetailsModel>?) {
        activity.get()?.let{safeActivity ->
            val intent = Intent(safeActivity, ImageDetailsActivity::class.java)
            intent.putParcelableArrayListExtra(ImageDetailsActivity.IMAGES_DATASET_KEY, ArrayList(imagesList))
            intent.putExtra(ImageDetailsActivity.SELECTED_ITEM_POSITION_KEY, selectedPosition)
            safeActivity.startActivity(intent)
        }
    }
}