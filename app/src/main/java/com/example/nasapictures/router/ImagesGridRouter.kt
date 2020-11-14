package com.example.nasapictures.router

import android.app.Activity
import android.content.Intent
import androidx.fragment.app.FragmentActivity
import com.example.nasapictures.extensions.isAvailable
import com.example.nasapictures.model.ImageDetailsModel
import com.example.nasapictures.view.activity.ImageDetailsActivity
import dagger.hilt.android.scopes.ActivityScoped
import java.lang.ref.WeakReference
import javax.inject.Inject

interface ImagesGridRouter{
    fun passDataToImagesDetails(selectedPosition:Int, imagesList: List<ImageDetailsModel>?)
}

class ImagesGridRouterImpl @Inject constructor(private val activity: FragmentActivity): ImagesGridRouter {

    override fun passDataToImagesDetails(selectedPosition:Int, imagesList: List<ImageDetailsModel>?) {
        if(activity.isAvailable()) {
            val intent = Intent(activity, ImageDetailsActivity::class.java)
            intent.putParcelableArrayListExtra(ImageDetailsActivity.IMAGES_DATASET_KEY, ArrayList(imagesList))
            intent.putExtra(ImageDetailsActivity.SELECTED_ITEM_POSITION_KEY, selectedPosition)
            activity.startActivity(intent)
        }
    }
}