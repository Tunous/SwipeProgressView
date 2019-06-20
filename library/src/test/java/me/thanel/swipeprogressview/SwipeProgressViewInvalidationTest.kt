package me.thanel.swipeprogressview

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.verify
import org.junit.Test

class SwipeProgressViewInvalidationTest : BaseSwipeProgressViewTest() {

    @Test
    fun `view is invalidated when progress changes`() {
        val view = buildSpyView()

        view.progress = 20

        verify(view).invalidate()
    }

    @Test
    fun `view is invalidated when max progress changes`() {
        val view = buildSpyView()

        view.maxProgress = 183

        verify(view).invalidate()
    }

    @Test
    fun `view is invalidated when min progress changes`() {
        val view = buildSpyView()

        view.minProgress = -234

        verify(view).invalidate()
    }

    @Test
    fun `view is invalidated when rtl mirroring changes`() {
        val view = buildSpyView()

        view.mirrorForRtl = true

        verify(view).invalidate()
    }

    @Test
    fun `view is invalidated when progress drawable changes`() {
        val view = buildSpyView()

        view.progressDrawable = ColorDrawable(Color.GRAY)

        verify(view).invalidate()
    }

    @Test
    fun `view is not invalidated when progress does not change`() {
        val view = buildSpyView()

        view.progress = view.progress

        verify(view, never()).invalidate()
    }

    @Test
    fun `view is not invalidated when max progress does not change`() {
        val view = buildSpyView()

        view.maxProgress = view.maxProgress

        verify(view, never()).invalidate()
    }

    @Test
    fun `view is not invalidated when min progress does not change`() {
        val view = buildSpyView()

        view.minProgress = view.minProgress

        verify(view, never()).invalidate()
    }

    @Test
    fun `view is not invalidated when rtl mirroring does not change`() {
        val view = buildSpyView()

        view.mirrorForRtl = view.mirrorForRtl

        verify(view, never()).invalidate()
    }

    @Test
    fun `view is not invalidated when progress drawable does not change`() {
        val view = buildSpyView()

        view.progressDrawable = view.progressDrawable

        verify(view, never()).invalidate()
    }
}
