package me.thanel.swipeprogressview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewConfiguration
import android.widget.FrameLayout
import androidx.annotation.ColorInt
import me.thanel.swipeprogressview.internal.clamp
import kotlin.math.abs
import kotlin.math.roundToInt

class SwipeProgressView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val touchSlop = ViewConfiguration.get(context).scaledTouchSlop
    private val progressBackground = ColorDrawable(getColor(context))
    private var onProgressChangeListener: ((Int) -> Unit)? = null
    private var isScrolling = false
    private var initialEventX = 0f
    private var initialEventY = 0f
    private var currentScrollStartValue = 0

    var minValue = 0

    var maxValue = 100

    var currentValue = 20
        set(value) {
            if (field == value) return
            field = value
            invalidate()
            onProgressChangeListener?.invoke(value)
        }

    init {
        setWillNotDraw(false)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        when (ev.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                initialEventX = ev.x
                initialEventY = ev.y
                currentScrollStartValue = currentValue
            }
            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                isScrolling = false
            }
            MotionEvent.ACTION_MOVE -> {
                val scrolledDistance = ev.x - initialEventX

                if (!isScrolling) {
                    val verticalScrolledDistance = ev.y - initialEventY
                    if (abs(verticalScrolledDistance) >= touchSlop) {
                        return false
                    }
                    isScrolling = abs(scrolledDistance) >= touchSlop
                }

                return isScrolling
            }
        }
        return false
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                isScrolling = false
            }
            MotionEvent.ACTION_MOVE -> {
                parent?.requestDisallowInterceptTouchEvent(true)

                val scrolledDistance = event.x - initialEventX

                // Convert that distance to represent percentage of the view's width
                val scrollScale = scrolledDistance / width
                // Multiply it by some factor to update the value a bit slower
                val factoredScrollScale = scrollScale * 0.5f
                // Convert the scale to actual value
                val scrolledValue = (maxValue * factoredScrollScale).roundToInt()
                // Add to it the value that was set before scroll started
                val updatedValue = scrolledValue + currentScrollStartValue
                // Finally clamp the value between min and max and notify the listener with result
                currentValue = updatedValue.clamp(minValue, maxValue)

                return true
            }
        }
        return super.onTouchEvent(event)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val progressPercent = currentValue / maxValue.toFloat()
        val end = ((right - left) * progressPercent).roundToInt()
        progressBackground.setBounds(0, 0, end, bottom - top)
        progressBackground.draw(canvas)
    }

    @ColorInt
    private fun getColor(context: Context): Int {
        val typedArray = context.obtainStyledAttributes(intArrayOf(R.attr.colorControlActivated))
        val color = typedArray.getColor(0, Color.BLACK)
        typedArray.recycle()
        return color
    }
}
