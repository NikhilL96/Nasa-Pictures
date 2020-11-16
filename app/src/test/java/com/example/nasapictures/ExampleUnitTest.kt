package com.example.nasapictures

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.nasapictures.interactor.ImagesGridInteractor
import com.example.nasapictures.model.ImageDetailsModel
import com.example.nasapictures.utils.DateUtils
import com.example.nasapictures.viewmodel.ImagesGridActivityViewModel
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

@RunWith(RobolectricTestRunner::class)
class ExampleUnitTest {

    private lateinit var viewModel: ImagesGridActivityViewModel

    @Before
    fun setup() {
        val context: Context = ApplicationProvider.getApplicationContext()
        viewModel = ImagesGridActivityViewModel(ImagesGridInteractorTestImpl(context))
    }

    @Test
    fun imagesSortedEarliestFirst() {
        Assert.assertTrue(!viewModel.imageList.isNullOrEmpty())
        viewModel.imageList?.let{
            Assert.assertTrue(isSorted(it))
        }
    }

    private fun isSorted(list: MutableList<ImageDetailsModel>): Boolean {
        for(index in 0 until list.size) {
            val imageDetailsModel = list[index]
            list.getOrNull(index+1)?.let{
                val currentImageDate = DateUtils.getDate(imageDetailsModel.date,
                    ImagesGridActivityViewModel.DATE_FORMAT
                )
                val nextImageDate = DateUtils.getDate(it.date,
                    ImagesGridActivityViewModel.DATE_FORMAT
                )
                if(currentImageDate?.compareTo(nextImageDate) == -1)  //should be greater or equal
                    return false
            }
        }
        return true
    }
}

class ImagesGridInteractorTestImpl (private val context: Context):
    ImagesGridInteractor {

    override fun getImagesList(): MutableList<ImageDetailsModel>? {
        return mutableListOf<ImageDetailsModel>(ImageDetailsModel(date = "2019-12-01"),
        ImageDetailsModel(date = "2019-11-01"), ImageDetailsModel(date = "2018-10-01"))
    }
}