package com.example.nasapictures.extensions

import android.animation.Animator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.Transformation
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.engine.Resource
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.example.nasapictures.R
import com.example.nasapictures.extensions.AnimationDuration.SHORT
import java.security.MessageDigest

object AnimationDuration {
    const val SHORT: Long = 300L
}

fun ImageView.loadFromUrl(
    imageUrl: String?,
    cache: Boolean = true,
    imageLoadFailure: (() -> Unit)? = null,
    imageLoadSuccessful: () -> Unit
) {
    if (context.isAvailable()) {
        val placeHolder = ContextCompat.getDrawable(context, R.drawable.ic_placeholder_image)
        val requestOptions =
            RequestOptions().placeholder(placeHolder).fallback(placeHolder)
                .diskCacheStrategy(if (cache) DiskCacheStrategy.AUTOMATIC else DiskCacheStrategy.NONE)
        Glide.with(context).asBitmap().load(imageUrl).apply(requestOptions)
            .listener(object : RequestListener<Bitmap> {

                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Bitmap>?,
                    isFirstResource: Boolean
                ): Boolean {
                    imageLoadFailure?.invoke()
                    return false
                }

                override fun onResourceReady(
                    resource: Bitmap?,
                    model: Any?,
                    target: Target<Bitmap>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    imageLoadSuccessful.invoke()
                    return false
                }

            }).into(this)
    }
}

fun View.hideWithAnimation() {
    this.alpha = 1.0f
    this.animate().alpha(0.0f).setDuration(SHORT).setListener(object : Animator.AnimatorListener {
        override fun onAnimationRepeat(animation: Animator?) {
        }
        override fun onAnimationEnd(animation: Animator?) {
            this@hideWithAnimation.visibility = View.GONE
        }
        override fun onAnimationCancel(animation: Animator?) {
        }
        override fun onAnimationStart(animation: Animator?) {
        }
    })
}

fun View.appearWithAnimation() {
    this.alpha = 0.0f
    this.visibility = View.VISIBLE
    this.animate().alpha(1.0f).setDuration(SHORT).setListener(null)
}

