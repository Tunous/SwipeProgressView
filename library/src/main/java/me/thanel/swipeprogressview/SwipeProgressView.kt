package me.thanel.swipeprogressview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.FrameLayout
import android.widget.SeekBar
import androidx.annotation.VisibleForTesting
import androidx.core.content.res.use
import me.thanel.swipeprogressview.internal.clamped
import me.thanel.swipeprogressview.internal.getColorFromAttr
import me.thanel.swipeprogressview.internal.isLayoutRtl
import kotlin.math.roundToInt

/**
 * An interactive user interface element that indicates generic progress. The user can touch the
 * view and swipe left or right to set the current progress level.
 *
 * A `SwipeProgressView` can also act as a container view in which you can place other interactive
 * or non interactive elements. Swipe actions will be available when swiping anywhere within this
 * view as long as child views (such as [SeekBar]) won't also attempt to listen to swipe gestures.
 *
 * You can update the percentage of progress displayed by using the [progress] property. By default,
 * the progress bar is full when the progress value reaches 100. You can adjust this default by
 * setting the `app:spv_maxProgress` attribute. You can also adjust the minimum value by setting
 * the `app:spv_minProgress` attribute.
 *
 * ## Samples
 *
 * Progress view with range from 25 to 200 and a default value of 50:
 *
 * ```xml
 * <me.thanel.swipeprogressview.SwipeProgressView
 *     android:layout_width="match_parent"
 *     android:layout_height="match_parent"
 *     android:progress="50"
 *     app:spv_maxProgress="200"
 *     app:spv_minProgress="25" />
 * ```
 *
 * Progress view that displays progress using solid black color:
 *
 * ```xml
 * <me.thanel.swipeprogressview.SwipeProgressView
 *     android:layout_width="match_parent"
 *     android:layout_height="match_parent"
 *     android:progressDrawable="@android:color/black" />
 * ```
 *
 * ### Attributes
 *
 * `app:spv_minProgress` - the lower limit of this progress view's range. Defaults to `0`.
 *
 * `app:spv_maxProgress` - the upper limit of this progress view's range. Defaults to `100`.
 *
 * `android:progress` - the progress view's current level of progress. Defaults to the same value
 * as `app:spv_minProgress`.
 *
 * `android:progressDrawable` - the drawable used to draw the progress indicator. Defaults to
 * [ColorDrawable] with color set from `colorControlActivated` style attribute.
 *
 * `android:mirrorForRtl` - defines if the progress drawable and swipe direction need to be
 * mirrored when in RTL mode. Defaults to `false`.
 */
@SuppressLint("Recycle")
class SwipeProgressView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.swipeProgressViewStyle
) : FrameLayout(context, attrs, defStyleAttr) {

    private val isLayoutRtl by lazy { isLayoutRtl() }
    private val shouldMirrorView get() = mirrorForRtl && isLayoutRtl
    private var isSwiping = false
    private var currentScrollStartValue = 0
    private var currentDownEvent: MotionEvent? = null
    private val defaultDrawable by lazy {
        ColorDrawable(context.getColorFromAttr(R.attr.colorControlActivated))
    }

    @VisibleForTesting
    internal var onProgressChangeListener: ((Int) -> Unit)? = null

    internal var visualProgress: Int = 0
        set(newValue) {
            if (field == newValue) return
            field = newValue
            invalidate()
        }

    /**
     * The upper limit of this progress view's range.
     *
     * Setting this property will also update the [progress] and [minProgress] values to not be
     * bigger than the newly set max progress.
     *
     * Can also be specified using `app:spv_maxProgress` attribute.
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
            invalidate()
        }

    /**
     * The lower limit of this progress view's range.
     *
     * Setting this property will also update the [progress] and [maxProgress] values to not be
     * smaller than the newly set min progress.
     *
     * Can also be specified using `app:spv_minProgress` attribute.
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
            invalidate()
        }

    /**
     * The progress view's current level of progress.
     *
     * Setting this property will immediately update the visual position of the progress indicator.
     * To animate the visual position to the target value, use [setProgressAnimated].
     *
     * **Note:** When attempting to set this value to any number outside of the [minProgress] and
     * [maxProgress] range, the value will be automatically set to the closest valid number that
     * fits within that range.
     *
     * Can also be specified using `android:progress` attribute.
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
     * Defines if the progress drawable and swipe direction need to be mirrored when in RTL mode.
     * Defaults to `false`.
     *
     * Can also be specified using `android:mirrorForRtl` attribute.
     */
    var mirrorForRtl = false
        set(newValue) {
            if (field == newValue) return
            field = newValue
            invalidate()
        }

    /**
     * The drawable used to draw the progress indicator.
     *
     * Defaults to [ColorDrawable] with color read from `colorControlActivated` style attribute.
     *
     * Can also be specified using `android:progressDrawable` attribute.
     *
     * For convenience you can also use the [setProgressColor] function to set the progress
     * drawable to a solid color.
     *
     * @see setProgressColor
     */
    var progressDrawable: Drawable = defaultDrawable
        set(newValue) {
            if (field == newValue) return
            field = newValue
            invalidate()
        }

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
            progressDrawable = drawable ?: defaultDrawable

            mirrorForRtl =
                it.getBoolean(R.styleable.SwipeProgressView_android_mirrorForRtl, mirrorForRtl)
        }
    }

    /**
     * Registers a callback to be invoked when the value of [progress] property is changed.
     *
     * @param listener the callback that will be run. Can be set to `null` to remove the listener.
     *
     * @see progress
     */
    fun setOnProgressChangeListener(listener: ((Int) -> Unit)?) {
        onProgressChangeListener = listener
    }

    private val gestureListener = object : GestureDetector.SimpleOnGestureListener() {
        override fun onDown(e: MotionEvent): Boolean {
            currentDownEvent = MotionEvent.obtain(e)
            isSwiping = false
            return true
        }

        override fun onScroll(
            e1: MotionEvent,
            e2: MotionEvent,
            distanceX: Float,
            distanceY: Float
        ): Boolean {
            if (!isSwiping && distanceX != 0f) {
                isSwiping = true
            }
            if (!isSwiping) return false

            parent?.requestDisallowInterceptTouchEvent(true)

            val scrolledDistance = if (shouldMirrorView) {
                e1.x - e2.x
            } else {
                e2.x - e1.x
            }
            val scrollScale = scrolledDistance / width

            val fullProgress = maxProgress - minProgress
            val factoredScrollScale = if (fullProgress > 2) {
                // Multiply scroll scale by a factor to make scrolling a bit slower giving the
                // user more precision during swipes. For example multiplying it by 0.5 will make
                // single swipe through the whole view update the progress by 50% instead of 100%.
                scrollScale * SCROLL_FACTOR
            } else {
                // But if the progress range is very small divide scroll scale by a factor instead
                // to make swiping faster.
                scrollScale / SCROLL_FACTOR
            }
            val scrolledValue = (fullProgress * factoredScrollScale).roundToInt()

            // Add the initial value to the calculated value so the update always changes as an
            // offset from the progress value before the swipe started instead of as an offset
            // from min progress value.
            val updatedValue = scrolledValue + currentScrollStartValue
            progress = updatedValue.clamped(minProgress, maxProgress)

            return true
        }
    }

    private val gestureDetector = GestureDetector(context, gestureListener)

    /**
     * @suppress
     */
    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        val result = gestureDetector.onTouchEvent(ev)
        if (ev.action == MotionEvent.ACTION_DOWN) {
            currentScrollStartValue = progress
            return false
        }
        return result
    }

    /**
     * @suppress
     */
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val result = gestureDetector.onTouchEvent(event)
        if (event.action == MotionEvent.ACTION_UP) {
            if (isSwiping) {
                val downEvent = currentDownEvent
                if (downEvent != null) {
                    gestureListener.onScroll(downEvent, event, 0f, 0f)
                }
            }
            return true
        }
        return result
    }

    /**
     * @suppress
     */
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
    }
}

