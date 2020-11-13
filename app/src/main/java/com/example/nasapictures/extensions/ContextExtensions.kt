package com.example.nasapictures.extensions

import android.app.Activity
import android.app.Application
import android.content.Context
import androidx.fragment.app.FragmentActivity


fun Context?.isAvailable(): Boolean {
    this?.let {
        if (this is Activity)
            return !this.isDestroyed && !this.isFinishing
        return true //for application context
    }
    return false
}
