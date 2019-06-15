package me.thanel.swipeprogressview

/**
 * TODO: Documentation
 */
var SwipeProgressView.progressRange: IntRange
    get() = minProgress..maxProgress
    set(newValue) {
        minProgress = newValue.start
        maxProgress = newValue.endInclusive
    }
