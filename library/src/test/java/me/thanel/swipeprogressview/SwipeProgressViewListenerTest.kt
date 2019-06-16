package me.thanel.swipeprogressview

import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.not
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test

class SwipeProgressViewListenerTest : BaseSwipeProgressViewTest() {

    private lateinit var view: SwipeProgressView

    @Before
    fun setupView() {
        view = buildView()
    }

    @Test
    fun `by default progress listener is set to null`() {
        assertThat(view.onProgressChangeListener, nullValue())
    }

    @Test
    fun `can set progress listener`() {
        val listener: (Int) -> Unit = {}

        view.setOnProgressChangeListener(listener)

        assertThat(view.onProgressChangeListener, equalTo(listener))
    }

    @Test
    fun `can modify progress listener`() {
        val originalListener: (Int) -> Unit = {}
        view.setOnProgressChangeListener(originalListener)

        val listener: (Int) -> Unit = {}
        view.setOnProgressChangeListener(listener)

        assertThat(view.onProgressChangeListener, not(equalTo(originalListener)))
        assertThat(view.onProgressChangeListener, equalTo(listener))
    }

    @Test
    fun `changing progress calls listener`() {
        var reportedProgress: Int? = null
        view.setOnProgressChangeListener {
            reportedProgress = it
        }

        view.progress = 15

        assertThat(reportedProgress, equalTo(15))
    }

    @Test
    fun `changing min value which results in progress change calls listener`() {
        var reportedProgress: Int? = null
        view.progress = 10
        view.setOnProgressChangeListener {
            reportedProgress = it
        }

        view.minProgress = 20

        assertThat(reportedProgress, equalTo(20))
    }

    @Test
    fun `changing max value which results in progress change calls listener`() {
        var reportedProgress: Int? = null
        view.progress = 30
        view.setOnProgressChangeListener {
            reportedProgress = it
        }

        view.maxProgress = 10

        assertThat(reportedProgress, equalTo(10))
    }

    @Test
    fun `changing progress with animation calls listener`() {
        var reportedProgress: Int? = null
        view.setOnProgressChangeListener {
            reportedProgress = it
        }

        view.setProgressAnimated(32)

        assertThat(reportedProgress, equalTo(32))
    }

    @Test
    fun `changing progress with animation notifies listener only once`() {
        var listenerCallCount = 0
        view.setOnProgressChangeListener {
            listenerCallCount += 1
        }

        view.setProgressAnimated(20)

        assertThat(listenerCallCount, equalTo(1))
    }
}
