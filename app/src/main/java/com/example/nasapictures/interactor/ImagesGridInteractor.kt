package com.example.nasapictures.interactor

import android.app.Application
import android.content.Context
import com.example.nasapictures.model.ImageDetailsModel
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import dagger.hilt.android.scopes.ActivityScoped
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.StringWriter
import java.lang.Exception
import javax.inject.Inject

@ActivityRetainedScoped
interface ImagesGridInteractor {
    fun getImagesList(): MutableList<ImageDetailsModel>?
}

@ActivityRetainedScoped
class ImagesGridInteractorImpl @Inject constructor(@ApplicationContext private val context: Context): ImagesGridInteractor {

    override fun getImagesList(): MutableList<ImageDetailsModel>? {
        val moshi = Moshi.Builder().build()
        val mutableListParametrizedType = Types.newParameterizedType(MutableList::class.java, ImageDetailsModel::class.java)
        val jsonAdapter: JsonAdapter<MutableList<ImageDetailsModel>> = moshi.adapter(mutableListParametrizedType)
        return try {
            jsonAdapter.fromJson(getImageJson())
        } catch (ex: Exception) {
            null
        }
    }

    //could optionally be put into a separate repository
    private fun getImageJson(): String {
        val inputStream = context.resources.openRawResource(com.example.nasapictures.R.raw.nasa_images_data)

        val writer = StringWriter()
        val buffer = CharArray(1024)
        try {
            val reader = BufferedReader(InputStreamReader(inputStream, "UTF-8"))
            var n: Int
            while (true) {
                n = reader.read(buffer)
                if (n == -1) {
                    break
                }
                writer.write(buffer, 0, n)

            }
        } finally {
            inputStream.close()
        }
        return writer.toString()
    }
}