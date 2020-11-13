package com.example.nasapictures.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.nasapictures.model.ImageDetailsModel

class ImageDetailsActivityViewModel (val applicationContext: Application) : AndroidViewModel(applicationContext) {

    val imagesDataSet: MutableList<ImageDetailsModel> = mutableListOf()
    var selectedPosition = 0
}