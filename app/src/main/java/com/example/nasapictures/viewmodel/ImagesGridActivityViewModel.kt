package com.example.nasapictures.viewmodel

import android.app.Application
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.example.nasapictures.interactor.ImagesGridInteractor
import com.example.nasapictures.model.ImageDetailsModel
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.StringWriter
import java.lang.Exception
import javax.inject.Inject

class ImagesGridActivityViewModel @ViewModelInject constructor(val interactor: ImagesGridInteractor) : ViewModel() {

    val imageList: MutableList<ImageDetailsModel>? by lazy {
        interactor.getImagesList()
    }
}