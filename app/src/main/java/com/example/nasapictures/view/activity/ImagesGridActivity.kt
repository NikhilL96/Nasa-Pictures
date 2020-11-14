package com.example.nasapictures.view.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.nasapictures.R
import com.example.nasapictures.router.ImagesGridRouter
import com.example.nasapictures.viewmodel.ImagesGridActivityViewModel
import com.example.nasapictures.view.adapter.ImagesGridAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_images_list.*
import java.lang.ref.WeakReference
import javax.inject.Inject

@AndroidEntryPoint
class ImagesGridActivity : TemplateActivity() {

    private lateinit var imagesListRecyclerView: RecyclerView
    private lateinit var imagesListAdapter: ImagesGridAdapter
    private val viewModel: ImagesGridActivityViewModel by viewModels()
    @Inject lateinit var router: ImagesGridRouter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_images_list)
        setupUI()
    }

    private fun setupUI() {
        imagesListRecyclerView = image_list_recycler_view   //reason for not using synthetic variables directly
                                                            // is synthetic variables behave like weak references
                                                            // and are null when the activity is destroyed
                                                            // async calls might return results after the
                                                            // activity is destroyed causing a crash when tried
                                                            // to access these variables

        viewModel.imageList?.let{
            imagesListAdapter = ImagesGridAdapter(it) {
                router.passDataToImagesDetails(it, viewModel.imageList)
            }

            imagesListRecyclerView.adapter = imagesListAdapter
            val staggeredGridLayoutManager = StaggeredGridLayoutManager(2, RecyclerView.VERTICAL).apply {
                gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS
            }
            imagesListRecyclerView.layoutManager = staggeredGridLayoutManager
        }
    }
}