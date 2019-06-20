package me.thanel.swipeprogressview

import android.graphics.drawable.ColorDrawable
import androidx.annotation.ColorInt

/**
 * The minimum-maximum progress range of this view.
 *
 * Setting this property will modify both the [minProgress][SwipeProgressView.minProgress] and
 * [maxProgress][SwipeProgressView.maxProgress] properties.
 *
 * @see SwipeProgressView.minProgress
 * @see SwipeProgressView.maxProgress
 */
var SwipeProgressView.progressRange: IntRange
    get() = minProgress..maxProgress
    set(newValue) {
        minProgress = newValue.first
        maxProgress = newValue.last
    }

/**
 * Sets the [progressDrawable][SwipeProgressView.progressDrawable] of this view to a solid [color].
 *
 * Using this function will override the [progressDrawable][SwipeProgressView.progressDrawable]
 * with a [ColorDrawable] of the specified [color].
 *
 * @param color the color to use as a progress drawable.
 *
 * @see SwipeProgressView.progressDrawable
 */
fun SwipeProgressView.setProgressColor(@ColorInt color: Int) {
    progressDrawable = ColorDrawable(color)
}
