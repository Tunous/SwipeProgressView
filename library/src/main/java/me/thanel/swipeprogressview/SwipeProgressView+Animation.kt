package me.thanel.swipeprogressview

import android.animation.ObjectAnimator
import android.os.Build
import android.util.IntProperty
import android.util.Property
import android.view.animation.DecelerateInterpolator

private const val ANIMATION_DURATION = 80L
private val INTERPOLATOR by lazy { DecelerateInterpolator() }
private val VISUAL_PROGRESS by lazy {
    if (Build.VERSION.SDK_INT >= 24) {
        object : IntProperty<SwipeProgressView>("progress") {
            override fun setValue(view: SwipeProgressView, value: Int) {
                view.visualProgress = value
            }

            override fun get(view: SwipeProgressView) = view.visualProgress
        }
    } else {
        object : Property<SwipeProgressView, Int>(Int::class.java, "progress") {
            override fun set(view: SwipeProgressView, value: Int) {
                view.visualProgress = value
            }

            override fun get(view: SwipeProgressView) = view.visualProgress
        }
    }
}

/**
 * Sets the current progress to the specified value, animating the visual position between the
 * current and target values.
 *
 * Animation does not affect the result of [progress][SwipeProgressView.progress] property, which
 * will return the target value immediately after this function is called.
 *
 * @param progress the new progress value.
 *
 * @see SwipeProgressView.progress
 */
fun SwipeProgressView.setProgressAnimated(progress: Int) {
    val initialProgress = visualProgress
    this.progress = progress
    visualProgress = initialProgress

    val animator = ObjectAnimator.ofInt(
        this,
        VISUAL_PROGRESS,
        initialProgress,
        progress
    )
    animator.setAutoCancel(true)
    animator.duration = ANIMATION_DURATION
    animator.interpolator = INTERPOLATOR
    animator.start()
}
