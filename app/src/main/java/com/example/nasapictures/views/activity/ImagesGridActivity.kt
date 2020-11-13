package com.example.nasapictures.views.activity

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.nasapictures.R
import com.example.nasapictures.router.ImagesGridRouter
import com.example.nasapictures.viewmodel.ImagesGridActivityViewModel
import com.example.nasapictures.views.adapter.ImagesGridAdapter
import kotlinx.android.synthetic.main.activity_images_list.*
import java.lang.ref.WeakReference

class ImagesGridActivity : TemplateActivity() {

    private lateinit var imagesListRecyclerView: RecyclerView
    private lateinit var imagesListAdapter: ImagesGridAdapter
    private lateinit var viewModel: ImagesGridActivityViewModel
    private val router = ImagesGridRouter(WeakReference(this))
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_images_list)

        viewModel = ViewModelProvider(this).get(ImagesGridActivityViewModel::class.java)
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