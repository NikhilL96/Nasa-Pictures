package com.example.nasapictures.views.component

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.view.animation.Animation
import android.view.animation.Transformation
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.view.GestureDetectorCompat
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.FlingAnimation
import kotlin.math.abs
import kotlin.math.min

private const val DEBUG_TAG = "Gestures"

open class ZoomLayout : FrameLayout, ScaleGestureDetector.OnScaleGestureListener,
    GestureDetector.OnGestureListener,
    GestureDetector.OnDoubleTapListener {

    private lateinit var gestureDetector: GestureDetectorCompat
    private lateinit var scaleDetector: ScaleGestureDetector

    private var mode = Mode.NONE
    protected var scale = 1.0f
    private var lastScaleFactor = 0f

    private var startX = 0f
    private var startY = 0f

    private var dx = 0f
    private var dy = 0f
    private var prevDx = 0f
    private var prevDy = 0f
    private var maxDx = 0f
    private var maxDy = 0f
    private var pinchX = 0f
    private var pinchY = 0f

    private var imageIntrinsicHeight = 0f
    private var imageIntrinsicWidth = 0f
    private var layoutHeight = 0
    private var layoutWidth = 0
    private var xTranslationWindowSize = 0f
    private var yTranslationWindowSize = 0f

    private var flingXLimit = 0f
    private var flingYLimit = 0f

    private lateinit var flingAnimationX: FlingAnimation
    private lateinit var flingAnimationY: FlingAnimation

    var zoomEnabled = true

    private enum class Mode {
        NONE,
        DRAG,
        DOUBLE_TAP_ZOOM,
        PINCH_ZOOM
    }

    private fun init(context: Context) {
        gestureDetector = GestureDetectorCompat(context, this)
        gestureDetector.setOnDoubleTapListener(this)
        scaleDetector = ScaleGestureDetector(context, this)
    }

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        init(context)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        return if(isEnabled) super.dispatchTouchEvent(ev) else false
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {

        initializeProperties()
        scaleDetector.onTouchEvent(event)
        gestureDetector.onTouchEvent(event)

        when (event.action and MotionEvent.ACTION_MASK) {

            MotionEvent.ACTION_DOWN -> handleActionDownMotionEvent(event)

            MotionEvent.ACTION_MOVE -> handleActionMoveMotionEvent(event)

            MotionEvent.ACTION_POINTER_DOWN -> handlePinchAction(event)

            MotionEvent.ACTION_POINTER_UP -> mode = Mode.NONE

            MotionEvent.ACTION_UP -> handleActionUpMotionEvent(event)
        }

        return true
    }

    private fun initializeProperties() {
        imageIntrinsicHeight = getImageIntrinsicHeightForScale()
        imageIntrinsicWidth = getImageIntrinsicWidthForScale()

        maxDx = (child().width - child().width / scale) / 2 * scale
        maxDy = (child().height - child().height / scale) / 2 * scale

        if(!::flingAnimationX.isInitialized || !::flingAnimationY.isInitialized) {
            flingAnimationX = FlingAnimation(child(), DynamicAnimation.TRANSLATION_X)
            flingAnimationY = FlingAnimation(child(), DynamicAnimation.TRANSLATION_Y)
        }
    }

    override fun onDown(event: MotionEvent): Boolean {
        Log.d(DEBUG_TAG, "onDown: $event")
        return true
    }

    override fun onFling(
        event1: MotionEvent,
        event2: MotionEvent,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        Log.d(
            DEBUG_TAG,
            "onFling: ${event1.x} ,${event1.y} \n ${event2.x}, ${event2.y}\n $velocityX, $velocityY\n $xTranslationWindowSize, $yTranslationWindowSize"
        )

        flingAnimationX.apply {
            setStartVelocity(velocityX)
            setMinValue(-flingXLimit)
            setMaxValue(flingXLimit)
            addUpdateListener { animation, value, velocity ->
                Log.d("onFling:", "x updated:$value\n ${child().translationX}")
                dx = value
                prevDx = dx
                child().translationX = value
            }
            friction = 1.1f
            if (-flingXLimit < flingXLimit)
                start()
        }

        flingAnimationY.apply {
            setStartVelocity(velocityY)
            setMaxValue(flingYLimit)
            setMinValue(-flingYLimit)
            addUpdateListener { animation, value, velocity ->
                Log.d("onFling:", "y updated:$value\n ${child().translationY}")
                dy = value
                prevDy = dy
                child().translationY = value
            }
            friction = 1.5f
            if (-flingYLimit < flingYLimit)
                start()
        }
        return true
    }

    override fun onLongPress(event: MotionEvent) {
        Log.d(DEBUG_TAG, "onLongPress: $event")
    }

    override fun onScroll(
        event1: MotionEvent,
        event2: MotionEvent,
        distanceX: Float,
        distanceY: Float
    ): Boolean {
        Log.d(DEBUG_TAG, "onScroll: ${event1.x} ${event2.x}")
        return true
    }

    override fun onShowPress(event: MotionEvent) {
        Log.d(DEBUG_TAG, "onShowPress: $event")
    }

    override fun onSingleTapUp(event: MotionEvent): Boolean {
        Log.d(DEBUG_TAG, "onSingleTapUp: $event")
        return false
    }

    override fun onDoubleTap(event: MotionEvent): Boolean {
        Log.d(DEBUG_TAG, "onDoubleTap: $event")
        mode = Mode.DOUBLE_TAP_ZOOM

        initializeProperties()
        dx = event.x
        dy = event.y

        scale = if (scale < MAX_ZOOM)
            (scale * 2).coerceAtMost(MAX_ZOOM)
        else
            1f

        flingXLimit = (getImageIntrinsicWidthForScale(scale) - width) / 2
        flingYLimit = (getImageIntrinsicHeightForScale(scale) - height) / 2

        doubleTapZoomAnimation(child(), child().scaleX, scale)

        return true
    }

    override fun onDoubleTapEvent(event: MotionEvent): Boolean {
        Log.d(DEBUG_TAG, "onDoubleTapEvent: $event")
        return false
    }

    override fun onSingleTapConfirmed(event: MotionEvent): Boolean {
        Log.d(DEBUG_TAG, "onSingleTapConfirmed: $event")
        return true
    }

    protected open fun handlePinchAction(motionEvent: MotionEvent) {
        mode = Mode.PINCH_ZOOM
        pinchX = (motionEvent.getX(0) + motionEvent.getX(1)) / 2f
        pinchY = (motionEvent.getY(0) + motionEvent.getY(1)) / 2f
        Log.d(TAG, "pinched")
    }

    private fun handleActionDownMotionEvent(motionEvent: MotionEvent) {
        Log.i(TAG, "DOWN")
        if (scale > MIN_ZOOM) {
            mode = Mode.DRAG
            flingAnimationX.cancel()
            flingAnimationY.cancel()
            startX = motionEvent.x - prevDx
            startY = motionEvent.y - prevDy

        }
    }

    private fun handleActionMoveMotionEvent(motionEvent: MotionEvent) {
        if (mode == Mode.DRAG) {
            dx = motionEvent.x - startX
            dy = motionEvent.y - startY
            Log.d(DEBUG_TAG, "moved: ${motionEvent.y} $startY")
            if (scale >= MIN_ZOOM) {
                parent.requestDisallowInterceptTouchEvent(true)
                applyTranslation()
            }
        }
    }

    private fun handleActionUpMotionEvent(motionEvent: MotionEvent) {
        mode = Mode.NONE
        prevDx = dx
        prevDy = dy
    }

    override fun onScaleBegin(scaleDetector: ScaleGestureDetector): Boolean {
        Log.i(TAG, "onScaleBegin")
        return true
    }

    override fun onScale(scaleDetector: ScaleGestureDetector): Boolean {

        Log.i(TAG, "onScale: $scaleDetector.scaleFactor, $scale")

        if (lastScaleFactor == 0f || Math.signum(scaleDetector.scaleFactor) == Math.signum(
                lastScaleFactor
            ) && abs(scaleDetector.scaleFactor - 1) > 0.0005
        ) {
            scale *= scaleDetector.scaleFactor
            scale = scale.coerceIn(MIN_ZOOM, MAX_ZOOM)
            lastScaleFactor = scaleDetector.scaleFactor
            child().scaleX = scale
            child().scaleY = scale

            dx = getLimitedXTranslation(
                (getImageIntrinsicWidthForScale(scale) * (1 - getClickedHorizontalRatio(pinchX))) - (getImageIntrinsicWidthForScale(
                    scale
                ) / 2) - (layoutWidth - pinchX) + layoutWidth / 2,
                getImageIntrinsicWidthForScale(scale),
                getMaxDxForScale(scale)
            )
            prevDx = dx

            dy = getLimitedYTranslation(
                (getImageIntrinsicHeightForScale(scale) * (1 - getClickedVerticalRatio(pinchY))) - (getImageIntrinsicHeightForScale(
                    scale
                ) / 2) - (layoutHeight - pinchY) + layoutHeight / 2,
                getImageIntrinsicHeightForScale(scale),
                getMaxDyForScale(scale)
            )
            prevDy = dy

            child().translationX = dx
            child().translationY = dy

        } else {
            lastScaleFactor = 0f
        }
        return true
    }

    override fun onScaleEnd(scaleDetector: ScaleGestureDetector) {
        Log.i(TAG, "onScaleEnd")
        flingXLimit = (getImageIntrinsicWidthForScale(scale) - width) / 2
        flingYLimit = (getImageIntrinsicHeightForScale(scale) - height) / 2
    }

    private fun doubleTapZoomAnimation(view: View, initialScale: Float, targetScale: Float) {
        val initialTranslationX = view.translationX
        val initialTranslationY = view.translationY

        layoutHeight = this.height
        layoutWidth = this.width

        mode = Mode.NONE

        val targetTranslationY = getLimitedYTranslation(
            (getImageIntrinsicHeightForScale(targetScale) * (1 - getClickedVerticalRatio(dy))) - (getImageIntrinsicHeightForScale(
                targetScale
            ) / 2) - (layoutHeight - dy) + layoutHeight / 2,
            getImageIntrinsicHeightForScale(targetScale),
            getMaxDyForScale(targetScale)
        )

        val targetTranslationX = getLimitedXTranslation(
            (getImageIntrinsicWidthForScale(targetScale) * (1 - getClickedHorizontalRatio(dx))) - (getImageIntrinsicWidthForScale(
                targetScale
            ) / 2) - (layoutWidth - dx) + layoutWidth / 2,
            getImageIntrinsicWidthForScale(targetScale),
            getMaxDxForScale(targetScale)
        )

        val a = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                if (initialScale < targetScale) {
                    scale = initialScale + (targetScale - initialScale) * interpolatedTime
                    view.scaleX = scale
                    view.scaleY = scale

                    initializeProperties()

                    dx =
                        initialTranslationX + ((targetTranslationX - initialTranslationX) * interpolatedTime)
                    prevDx = dx
                    view.translationX = dx

                    dy =
                        initialTranslationY + ((targetTranslationY - initialTranslationY) * interpolatedTime)
                    prevDy = dy
                    view.translationY = dy
                } else {
                    view.scaleX = initialScale - (initialScale - targetScale) * interpolatedTime
                    view.scaleY = initialScale - (initialScale - targetScale) * interpolatedTime
                    view.translationX =
                        initialTranslationX - (initialTranslationX * interpolatedTime)
                    view.translationY =
                        initialTranslationY - (initialTranslationY * interpolatedTime)
                }
            }

            override fun willChangeBounds(): Boolean {
                return true
            }
        }

        a.duration = ZOOM_ANIMATION_DURATION
        view.startAnimation(a)
    }

    private fun applyTranslation() {
        dx = getLimitedXTranslation()
        prevDx = dx
        child().translationX = dx

        dy = getLimitedYTranslation()
        prevDy = dy
        child().translationY = dy
        Log.d("Details", "${child().translationX},${dx}")
    }

    private fun getLimitedYTranslation(
        y: Float = dy,
        imageIntrinsicHeight: Float = this.imageIntrinsicHeight,
        maxDy: Float = this.maxDy
    ): Float {
        layoutHeight = this.height
        yTranslationWindowSize = min((imageIntrinsicHeight - layoutHeight) / 2, maxDy)
        yTranslationWindowSize = yTranslationWindowSize.coerceAtLeast(0f)
        return y.coerceIn(
            -yTranslationWindowSize,
            yTranslationWindowSize
        )
    }

    private fun getLimitedXTranslation(
        x: Float = dx,
        imageIntrinsicWidth: Float = this.imageIntrinsicWidth,
        maxDx: Float = this.maxDx
    ): Float {
        layoutWidth = this.width
        xTranslationWindowSize = min((imageIntrinsicWidth - layoutWidth) / 2, maxDx)
        xTranslationWindowSize = xTranslationWindowSize.coerceAtLeast(0f)
        return x.coerceIn(-xTranslationWindowSize, xTranslationWindowSize)
    }

    protected fun child(): View {
        return getChildAt(0)
    }

    private fun getImageIntrinsicHeightForScale(scale: Float = this.scale): Float {
        return getImage().drawable.intrinsicHeight.times(scale)
    }

    private fun getImageIntrinsicWidthForScale(scale: Float = this.scale): Float {
        return getImage().drawable.intrinsicWidth.times(scale)
    }

    private fun getMaxDxForScale(scale: Float): Float {
        return (child().width - child().width / scale) / 2 * scale
    }

    private fun getMaxDyForScale(scale: Float): Float {
        return (child().height - child().height / scale) / 2 * scale
    }

    private fun getClickedVerticalRatio(
        y: Float,
        imageIntrinsicHeight: Float = this.imageIntrinsicHeight
    ): Float {
        return ((imageIntrinsicHeight - layoutHeight) / 2 - child().translationY + y) / (imageIntrinsicHeight)
    }

    private fun getClickedHorizontalRatio(
        x: Float,
        imageIntrinsicWidth: Float = this.imageIntrinsicWidth
    ): Float {
        return ((imageIntrinsicWidth - layoutWidth) / 2 - child().translationX + x) / (imageIntrinsicWidth)
    }

    open fun getImage(): ImageView {
        return child() as ImageView
    }

    companion object {
        private const val TAG = "ZoomLayout"
        private const val MIN_ZOOM = 1.0f
        private const val MAX_ZOOM = 4.0f
        private const val ZOOM_ANIMATION_DURATION: Long = 300
    }
}
