package me.thanel.swipeprogressview

import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RectShape
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class SwipeProgressViewDrawableTest : BaseSwipeProgressViewTest() {

    @Test
    fun `by default progressDrawable is set to ColorDrawable with color from colorControlActivated attribute`() {
        val typedArray = mock<TypedArray>()
        whenever(typedArray.getColor(eq(0), any())).thenReturn(Color.RED)
        whenever(context.obtainStyledAttributes(intArrayOf(R.attr.colorControlActivated)))
            .thenReturn(typedArray)

        val view = buildView()

        assertIsColorDrawableOfColor(view.progressDrawable, Color.RED)
    }

    @Test
    fun `progressDrawable can be set using attributes`() {
        val drawable: Drawable = ShapeDrawable(RectShape())
        val view = buildView {
            mockDrawableAttr(R.styleable.SwipeProgressView_android_progressDrawable, drawable)
        }

        assertThat(view.progressDrawable, equalTo(drawable))
    }
}
