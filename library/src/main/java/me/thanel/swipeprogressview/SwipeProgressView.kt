package me.thanel.swipeprogressview

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.util.IntProperty
import android.util.Property
import android.view.MotionEvent
import android.view.ViewConfiguration
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import androidx.core.content.res.use
import me.thanel.swipeprogressview.internal.clamped
import me.thanel.swipeprogressview.internal.getColorFromAttr
import me.thanel.swipeprogressview.internal.isLayoutRtl
import kotlin.math.abs
import kotlin.math.roundToInt

@SuppressLint("Recycle")
class SwipeProgressView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.swipeProgressViewStyle
) : FrameLayout(context, attrs, defStyleAttr) {

    private val touchSlop = ViewConfiguration.get(context).scaledTouchSlop
    private val isLayoutRtl by lazy { isLayoutRtl() }
    private val shouldMirrorView get() = mirrorForRtl && isLayoutRtl
    private var onProgressChangeListener: ((Int) -> Unit)? = null
    private var isSwiping = false
    private var initialEventX = 0f
    private var initialEventY = 0f
    private var currentScrollStartValue = 0
    private var mirrorForRtl = false

    private var visualProgress: Int = 0
        set(newValue) {
            field = newValue
            invalidate()
        }

    /**
     * The lower limit of this progress view's range.
     *
     * Setting this property will also update the [currentProgress] value to not be smaller than the
     * newly set min progress.
     *
     * @see currentProgress
     * @see maxProgress
     */
    var minProgress: Int = 0
        set(newValue) {
            if (field == newValue) return
            field = newValue
            if (currentProgress < newValue) {
                currentProgress = newValue
            }
        }

    /**
     * The upper limit of this progress view's range.
     *
     * Setting this property will also update the [currentProgress] value to not be bigger than the
     * newly set max progress.
     *
     * @see currentProgress
     * @see minProgress
     */
    var maxProgress: Int = 100
        set(newValue) {
            if (field == newValue) return
            field = newValue
            if (currentProgress > newValue) {
                currentProgress = newValue
            }
        }

    /**
     * The progress view's current level of progress.
     *
     * Setting this property will immediately update the visual position of the progress indicator.
     * To animate the visual position to the target value, use [setCurrentProgressAnimated].
     *
     * **Note** When attempting to set this value to any number outside of the [minProgress] and
     * [maxProgress] range, the value will be automatically set to the closest valid number that
     * fits within that range.
     *
     * @see minProgress
     * @see maxProgress
     * @see setCurrentProgressAnimated
     */
    var currentProgress: Int = 0
        set(newValue) {
            if (field == newValue) return
            field = when {
                newValue > maxProgress -> maxProgress
                newValue < minProgress -> minProgress
                else -> newValue
            }
            visualProgress = field
            onProgressChangeListener?.invoke(newValue)
        }

    /**
     * The drawable used to draw the progress indicator.
     */
    lateinit var progressDrawable: Drawable

    init {
        setWillNotDraw(false)

        context.obtainStyledAttributes(attrs, R.styleable.SwipeProgressView).use {
            minProgress = it.getInt(R.styleable.SwipeProgressView_spv_minProgress, minProgress)
            maxProgress = it.getInt(R.styleable.SwipeProgressView_spv_maxProgress, maxProgress)
            currentProgress =
                it.getInt(R.styleable.SwipeProgressView_spv_currentProgress, currentProgress)
            val drawable = it.getDrawable(R.styleable.SwipeProgressView_android_progressDrawable)
            progressDrawable =
                drawable ?: ColorDrawable(context.getColorFromAttr(R.attr.colorControlActivated))
            mirrorForRtl =
                it.getBoolean(R.styleable.SwipeProgressView_android_mirrorForRtl, mirrorForRtl)
        }
    }

    /**
     * Registers a callback to be invoked when the value of [currentProgress] property is changed.
     *
     * @param listener the callback that will be run.
     *
     * @see currentProgress
     */
    fun setOnProgressChangeListener(listener: ((Int) -> Unit)?) {
        onProgressChangeListener = listener
    }

    /**
     * Sets the current progress to the specified value, animating the visual position between the
     * current and target values.
     *
     * Animation does not affect the result of [currentProgress] property, which will return the
     * target value immediately after this function is called.
     *
     * @param progress the new progress value.
     *
     * @see currentProgress
     */
    fun setCurrentProgressAnimated(progress: Int) {
        val initialProgress = visualProgress
        currentProgress = progress
        visualProgress = initialProgress

        val animator = ObjectAnimator.ofInt(this, VISUAL_PROGRESS, initialProgress, progress)
        animator.setAutoCancel(true)
        animator.duration = ANIMATION_DURATION
        animator.interpolator = INTERPOLATOR
        animator.start()
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        when (ev.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                initialEventX = ev.x
                initialEventY = ev.y
                currentScrollStartValue = currentProgress
            }
            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                isSwiping = false
            }
            MotionEvent.ACTION_MOVE -> {
                val scrolledDistance = ev.x - initialEventX

                if (!isSwiping) {
                    val verticalScrolledDistance = ev.y - initialEventY
                    if (abs(verticalScrolledDistance) >= touchSlop) {
                        return false
                    }
                    isSwiping = abs(scrolledDistance) >= touchSlop
                }

                return isSwiping
            }
        }
        return false
    }

    @SuppressLint("ClickableViewAccessibility") // performClick is not used
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                isSwiping = false
            }
            MotionEvent.ACTION_MOVE -> {
                parent?.requestDisallowInterceptTouchEvent(true)

                val scrolledDistance = if (shouldMirrorView) {
                    initialEventX - event.x
                } else {
                    event.x - initialEventX
                }
                val scrollScale = scrolledDistance / width

                // Multiply scroll scale by a factor to make scrolling a bit slower giving the
                // user more precision during swipes. For example multiplying it by 0.5 will make
                // single swipe through the whole view update the progress by 50% instead of 100%.
                val factoredScrollScale = scrollScale * SCROLL_FACTOR
                val scrolledValue = (maxProgress * factoredScrollScale).roundToInt()

                // Add the initial value to the calculated value so the update always changes as an
                // offset from the progress value before the swipe started instead of as an offset
                // from min progress value.
                val updatedValue = scrolledValue + currentScrollStartValue
                currentProgress = updatedValue.clamped(minProgress, maxProgress)

                return true
            }
        }
        return super.onTouchEvent(event)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val progressPercent =
            (visualProgress - minProgress) / (maxProgress - minProgress).toFloat()
        val progressWidth = (width * progressPercent).roundToInt()
        if (shouldMirrorView) {
            progressDrawable.setBounds(width - progressWidth, 0, right, height)
        } else {
            progressDrawable.setBounds(0, 0, progressWidth, height)
        }
        progressDrawable.draw(canvas)
    }

    companion object {
        private const val SCROLL_FACTOR = 0.5f
        private const val ANIMATION_DURATION = 80L
        private val INTERPOLATOR = DecelerateInterpolator()
        private val VISUAL_PROGRESS = if (Build.VERSION.SDK_INT >= 24) {
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
}

