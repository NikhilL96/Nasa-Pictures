package com.example.nasapictures.views.component

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.widget.ImageView
import com.example.nasapictures.R

class ImageCarouselZoomLayout: ZoomLayout {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    )

    private var onZoomLayoutClicked: (() -> Unit)? = null
    private var scaleChangeListener: ((canScroll: Boolean) -> Unit)? = null


    fun assignZoomClickListener(clickAction: () -> Unit) {
        onZoomLayoutClicked = clickAction
    }

    fun assignScaleChangeListener(onScaleChange: (Boolean) -> Unit) {
        scaleChangeListener = onScaleChange
    }

    override fun onSingleTapConfirmed(event: MotionEvent): Boolean {
        onZoomLayoutClicked?.invoke()
        return super.onSingleTapConfirmed(event)
    }

    override fun handlePinchAction(motionEvent: MotionEvent) {
        scaleChangeListener?.invoke(false)
        super.handlePinchAction(motionEvent)
    }

    override fun onScaleEnd(scaleDetector: ScaleGestureDetector) {
        scaleChangeListener?.invoke(scale==1f)
        super.onScaleEnd(scaleDetector)
    }

    override fun onDoubleTap(event: MotionEvent): Boolean {
        super.onDoubleTap(event)
        scaleChangeListener?.invoke(scale==1f)
        return true
    }

    override fun getImage(): ImageView {
        return child().findViewById(R.id.image_carousel_zoom_image)
    }
}