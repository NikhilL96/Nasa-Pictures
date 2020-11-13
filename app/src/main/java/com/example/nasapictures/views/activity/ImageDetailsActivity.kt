package com.example.nasapictures.views.activity

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.nasapictures.R
import com.example.nasapictures.extensions.appearWithAnimation
import com.example.nasapictures.extensions.hideWithAnimation
import com.example.nasapictures.model.ImageDetailsModel
import com.example.nasapictures.viewmodel.ImageDetailsActivityViewModel
import com.example.nasapictures.views.adapter.ImageCarouselAdapter
import com.example.nasapictures.views.component.SnapOnScrollListener
import com.example.nasapictures.views.fragment.ImageDetailsDialogFragment
import kotlinx.android.synthetic.main.activity_image_details.*

class ImageDetailsActivity : TemplateActivity(), SnapOnScrollListener.OnSnapPositionChangeListener {

    companion object {
        const val IMAGES_DATASET_KEY = "images_dataset"
        const val SELECTED_ITEM_POSITION_KEY = "selected_position"
    }

    private lateinit var imageCarouselRecycler: RecyclerView
    private lateinit var imageCarouselAdapter: ImageCarouselAdapter
    private lateinit var imageCountText: TextView
    private lateinit var exitActivityButton: ImageView
    private lateinit var imageInfoButton: ImageView
    private lateinit var viewModel: ImageDetailsActivityViewModel
    private var visibleImagePosition = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_details)
        viewModel = ViewModelProvider(this).get(ImageDetailsActivityViewModel::class.java)
        getIntentData()
        makeActivityFullScreen()
        setupUI()
    }


    private fun getIntentData() {
        intent.getParcelableArrayListExtra<ImageDetailsModel>(IMAGES_DATASET_KEY)?.let{
            viewModel.imagesDataSet.addAll(it)
        }
        viewModel.selectedPosition = intent.getIntExtra(SELECTED_ITEM_POSITION_KEY, 0)
    }

    private fun setupUI() {

        initializeViews()
        initializeListeners()
        initializeRecycler()
    }

    private fun initializeViews() {
        imageCarouselRecycler = image_carousel_recycler_view
        imageCountText = image_count_tv
        exitActivityButton = exit_image_details_button
        imageInfoButton = image_details_button
    }

    private fun initializeListeners() {
        exitActivityButton.setOnClickListener {
            finish()
        }
        imageInfoButton.setOnClickListener {
            viewModel.imagesDataSet.getOrNull(visibleImagePosition)?.let{
                val fragment = ImageDetailsDialogFragment.newInstance(it)
                if (!fragment.isAdded)
                    fragment.show(supportFragmentManager, "transaction_summary_fragment")
            }
        }
    }

    private fun initializeRecycler() {
        imageCarouselAdapter = ImageCarouselAdapter(this, viewModel.imagesDataSet, onZoomLayoutClick)
        imageCarouselRecycler.layoutManager = object :
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false) {
            override fun canScrollHorizontally(): Boolean {
                return imageCarouselAdapter.getCanScroll()
            }
        }
        imageCarouselRecycler.adapter = imageCarouselAdapter
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(imageCarouselRecycler)
        val snapOnScrollListener =
            SnapOnScrollListener(snapHelper, SnapOnScrollListener.Behavior.NOTIFY_ON_SCROLL, this)
        imageCarouselRecycler.addOnScrollListener(snapOnScrollListener)
        imageCarouselRecycler.scrollToPosition(viewModel.selectedPosition)
        updateImageCountTextView(viewModel.selectedPosition)
    }

    private val onZoomLayoutClick: () -> Unit = {

        if (exitActivityButton.visibility == View.GONE) {
            exitActivityButton.appearWithAnimation()
            imageCountText.appearWithAnimation()
            imageInfoButton.appearWithAnimation()

        } else {
            exitActivityButton.hideWithAnimation()
            imageCountText.hideWithAnimation()
            imageInfoButton.hideWithAnimation()
            makeActivityFullScreen()
        }
    }

    private fun makeActivityFullScreen() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_IMMERSIVE)
        }
        supportActionBar?.hide()
    }

    override fun onSnapPositionChange(position: Int) {
        updateImageCountTextView(position)
    }

    private fun updateImageCountTextView(position: Int) {
        visibleImagePosition = position
        imageCountText.text = getString(R.string.image_carousel_count, position + 1,
            viewModel.imagesDataSet.size)
    }
}