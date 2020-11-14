package com.example.nasapictures.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.example.nasapictures.model.ImageDetailsModel

class ImageDetailsActivityViewModel () : ViewModel() {

    val imagesDataSet: MutableList<ImageDetailsModel> = mutableListOf()
    var selectedPosition = 0

    fun getSelectedImageDetails(): ImageDetailsModel? {
        return imagesDataSet.getOrNull(selectedPosition)
    }
}