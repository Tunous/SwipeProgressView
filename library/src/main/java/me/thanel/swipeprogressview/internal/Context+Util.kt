package me.thanel.swipeprogressview.internal

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.ViewConfiguration
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.core.content.res.use

@ColorInt
@SuppressLint("Recycle")
internal fun Context.getColorFromAttr(@AttrRes attrResId: Int): Int {
    obtainStyledAttributes(intArrayOf(attrResId)).use {
        return it.getColor(0, Color.TRANSPARENT)
    }
}

internal val Context.scaledTouchSlop: Int
    get() = ViewConfiguration.get(this).scaledTouchSlop
