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
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import androidx.annotation.VisibleForTesting
import androidx.core.content.res.use
import me.thanel.swipeprogressview.internal.clamped
import me.thanel.swipeprogressview.internal.getColorFromAttr
import me.thanel.swipeprogressview.internal.isLayoutRtl
import me.thanel.swipeprogressview.internal.scaledTouchSlop
import kotlin.math.abs
import kotlin.math.roundToInt

@SuppressLint("Recycle")
class SwipeProgressView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.swipeProgressViewStyle
) : FrameLayout(context, attrs, defStyleAttr) {

    private val touchSlop = context.scaledTouchSlop
    private val isLayoutRtl by lazy { isLayoutRtl() }
    private val shouldMirrorView get() = mirrorForRtl && isLayoutRtl
    private var isSwiping = false
    private var initialEventX = 0f
    private var initialEventY = 0f
    private var currentScrollStartValue = 0
    private var mirrorForRtl = false

    @VisibleForTesting
    internal var onProgressChangeListener: ((Int) -> Unit)? = null

    private var visualProgress: Int = 0
        set(newValue) {
            field = newValue
            invalidate()
        }

    /**
     * The lower limit of this progress view's range.
     *
     * Setting this property will also update the [progress] and [maxProgress] values to not be
     * smaller than the newly set min progress.
     *
     * @see progress
     * @see maxProgress
     */
    var minProgress: Int = 0
        set(newValue) {
            if (field == newValue) return
            field = newValue
            if (progress < newValue) {
                progress = newValue
            }
            if (maxProgress < newValue) {
                maxProgress = newValue
            }
        }

    /**
     * The upper limit of this progress view's range.
     *
     * Setting this property will also update the [progress] and [minProgress] values to not be
     * bigger than the newly set max progress.
     *
     * @see progress
     * @see minProgress
     */
    var maxProgress: Int = 100
        set(newValue) {
            if (field == newValue) return
            field = newValue
            if (progress > newValue) {
                progress = newValue
            }
            if (minProgress > newValue) {
                minProgress = newValue
            }
        }

    /**
     * The progress view's current level of progress.
     *
     * Setting this property will immediately update the visual position of the progress indicator.
     * To animate the visual position to the target value, use [setProgressAnimated].
     *
     * **Note** When attempting to set this value to any number outside of the [minProgress] and
     * [maxProgress] range, the value will be automatically set to the closest valid number that
     * fits within that range.
     *
     * @see minProgress
     * @see maxProgress
     * @see setProgressAnimated
     */
    var progress: Int = 0
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
            val minProgress = it.getInt(R.styleable.SwipeProgressView_spv_minProgress, minProgress)
            val maxProgress = it.getInt(R.styleable.SwipeProgressView_spv_maxProgress, maxProgress)
            val progress = it.getInt(R.styleable.SwipeProgressView_android_progress, minProgress)

            if (minProgress > maxProgress) {
                throw IllegalStateException("Min progress value ($minProgress) must be lower or equal to max progress value ($maxProgress)")
            }
            if (progress < minProgress || progress > maxProgress) {
                throw IllegalStateException("Progress ($progress) can't be a number outside of min-max range ($minProgress..$maxProgress)")
            }

            this.minProgress = minProgress
            this.maxProgress = maxProgress
            this.progress = progress

            val drawable = it.getDrawable(R.styleable.SwipeProgressView_android_progressDrawable)
            progressDrawable =
                drawable ?: ColorDrawable(context.getColorFromAttr(R.attr.colorControlActivated))
            mirrorForRtl =
                it.getBoolean(R.styleable.SwipeProgressView_android_mirrorForRtl, mirrorForRtl)
        }
    }

    /**
     * Registers a callback to be invoked when the value of [progress] property is changed.
     *
     * @param listener the callback that will be run.
     *
     * @see progress
     */
    fun setOnProgressChangeListener(listener: ((Int) -> Unit)?) {
        onProgressChangeListener = listener
    }

    /**
     * Sets the current progress to the specified value, animating the visual position between the
     * current and target values.
     *
     * Animation does not affect the result of [progress] property, which will return the target
     * value immediately after this function is called.
     *
     * @param progress the new progress value.
     *
     * @see progress
     */
    fun setProgressAnimated(progress: Int) {
        val initialProgress = visualProgress
        this.progress = progress
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
                currentScrollStartValue = progress
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
            MotionEvent.ACTION_CANCEL -> {
                isSwiping = false
            }
            MotionEvent.ACTION_MOVE, MotionEvent.ACTION_UP -> {
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
                progress = updatedValue.clamped(minProgress, maxProgress)

                if (event.actionMasked == MotionEvent.ACTION_UP) {
                    isSwiping = false
                }

                return true
            }
        }
        return true
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
    }
}

