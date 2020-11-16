package com.example.nasapictures.viewmodel

import android.app.Application
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.example.nasapictures.interactor.ImagesGridInteractor
import com.example.nasapictures.model.ImageDetailsModel
import com.example.nasapictures.utils.DateUtils
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.StringWriter
import java.lang.Exception
import javax.inject.Inject

class ImagesGridActivityViewModel @ViewModelInject constructor(val interactor: ImagesGridInteractor) : ViewModel() {

    companion object {
        const val DATE_FORMAT = "yyyy-MM-dd"
    }

    val imageList: MutableList<ImageDetailsModel>? by lazy {
        val imagesList = interactor.getImagesList()
        imagesList?.shuffle()
        imagesList?.sortWith(Comparator { image1, image2 ->
            val image1Date = DateUtils.getDate(image1.date, DATE_FORMAT)
            val image2Date = DateUtils.getDate(image2.date, DATE_FORMAT)
            image1Date?.compareTo(image2Date) ?:0
        })
        imagesList
    }

}