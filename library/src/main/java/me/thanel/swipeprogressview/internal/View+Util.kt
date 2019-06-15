package me.thanel.swipeprogressview.internal

import android.view.View
import androidx.core.view.ViewCompat

internal fun View.isLayoutRtl(): Boolean =
    ViewCompat.getLayoutDirection(this) == ViewCompat.LAYOUT_DIRECTION_RTL
