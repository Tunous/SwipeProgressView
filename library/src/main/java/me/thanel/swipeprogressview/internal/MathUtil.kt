package me.thanel.swipeprogressview.internal

import kotlin.math.max
import kotlin.math.min

internal fun Int.clamp(minValue: Int, maxValue: Int) = max(minValue, min(this, maxValue))
