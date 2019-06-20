package me.thanel.swipeprogressview

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
        minProgress = newValue.start
        maxProgress = newValue.endInclusive
    }
