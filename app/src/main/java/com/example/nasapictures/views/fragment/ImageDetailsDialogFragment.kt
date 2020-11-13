package com.example.nasapictures.views.fragment

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.nasapictures.R
import com.example.nasapictures.model.ImageDetailsModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.layout_image_details.*

class ImageDetailsDialogFragment : BottomSheetDialogFragment() {

    companion object {
        const val IMAGE_DETAILS_KEY = "image_detail"
        fun newInstance(imageDetails: ImageDetailsModel): ImageDetailsDialogFragment {
            return ImageDetailsDialogFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(IMAGE_DETAILS_KEY, imageDetails)
                }
            }
        }
    }

    private lateinit var titleTv: TextView
    private lateinit var descriptionTv: TextView
    private lateinit var urlTv: TextView
    private lateinit var dateTv: TextView
    private lateinit var versionTv: TextView
    private lateinit var copyrightTv: TextView
    private lateinit var backButton: ImageView

    private var imageDetails: ImageDetailsModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getBundledData()

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.layout_image_details, container,
            false
        )
    }

    private fun getBundledData() {
        arguments?.let{
            imageDetails = it.getParcelable<ImageDetailsModel>(IMAGE_DETAILS_KEY)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    private fun setupUI() {
        titleTv = image_details_title_tv

        descriptionTv = image_details_description_tv

        urlTv = image_details_url_tv

        dateTv = image_details_date_tv

        versionTv = image_details_version_tv

        copyrightTv = image_details_copyright_tv

        backButton = image_details_back_button

        populateData()
        initializeListeners()
    }

    private fun populateData() {
        titleTv.text = imageDetails?.title?:"-"
        descriptionTv.text = imageDetails?.explanation?:"-"
        urlTv.text = imageDetails?.hdurl?:"-"
        dateTv.text = imageDetails?.date?:"-"
        versionTv.text = imageDetails?.serviceVersion?:"-"
        copyrightTv.text = imageDetails?.copyright?:"-"
    }

    private fun initializeListeners() {
        backButton.setOnClickListener {
            dismiss()
        }
    }

}