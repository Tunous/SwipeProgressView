package me.thanel.swipeprogressview

import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert.assertThat
import org.junit.Test

class SwipeProgressViewProgressTest : BaseSwipeProgressViewTest() {

    @Test
    fun `by default progress is set to 0`() {
        val view = buildView()

        assertThat(view.progress, equalTo(0))
    }

    @Test
    fun `by default min progress is set to 0`() {
        val view = buildView()

        assertThat(view.minProgress, equalTo(0))
    }

    @Test
    fun `by default max progress is set to 100`() {
        val view = buildView()

        assertThat(view.maxProgress, equalTo(100))
    }

    @Test
    fun `can set progress to value that fits in range`() {
        val view = buildView()

        view.progress = 80

        assertThat(view.progress, equalTo(80))
    }

    @Test
    fun `can change min progress to any value`() {
        val view = buildView()

        view.minProgress = -200

        assertThat(view.minProgress, equalTo(-200))
    }

    @Test
    fun `can change max progress to any value`() {
        val view = buildView()

        view.maxProgress = 1000

        assertThat(view.maxProgress, equalTo(1000))
    }

    @Test
    fun `setting progress to value lower than min progress sets it to min progress`() {
        val view = buildView()
        view.minProgress = -50

        view.progress = -100

        assertThat(view.progress, equalTo(-50))
    }

    @Test
    fun `setting progress to value higher than max progress sets it to max progress`() {
        val view = buildView()
        view.maxProgress = 90

        view.progress = 1000

        assertThat(view.progress, equalTo(90))
    }

    @Test
    fun `setting min progress to value higher than max progress also modifies max progress`() {
        val view = buildView()
        view.maxProgress = 80

        view.minProgress = 100

        assertThat(view.minProgress, equalTo(100))
        assertThat(view.maxProgress, equalTo(100))
    }

    @Test
    fun `setting max progress to value lower than min progress also modifies min progress`() {
        val view = buildView()
        view.minProgress = 20

        view.maxProgress = 15

        assertThat(view.maxProgress, equalTo(15))
        assertThat(view.minProgress, equalTo(15))
    }

    @Test
    fun `min progress can be set using attributes`() {
        val view = buildView {
            mockIntAttr(R.styleable.SwipeProgressView_spv_minProgress, 90)
        }

        assertThat(view.minProgress, equalTo(90))
    }

    @Test
    fun `max progress can be set using attributes`() {
        val view = buildView {
            mockIntAttr(R.styleable.SwipeProgressView_spv_maxProgress, 289)
        }

        assertThat(view.maxProgress, equalTo(289))
    }

    @Test
    fun `progress can be set using attributes`() {
        val view = buildView {
            mockIntAttr(R.styleable.SwipeProgressView_android_progress, 56)
        }

        assertThat(view.progress, equalTo(56))
    }

    @Test(expected = IllegalStateException::class)
    fun `trying to set min progress to value higher than default max progress using attributes throws exception`() {
        buildView {
            mockIntAttr(R.styleable.SwipeProgressView_spv_minProgress, 200)
        }
    }

    @Test(expected = IllegalStateException::class)
    fun `trying to set max progress to value lower than default min progress using attributes throws exception`() {
        buildView {
            mockIntAttr(R.styleable.SwipeProgressView_spv_maxProgress, -100)
        }
    }

    @Test(expected = IllegalStateException::class)
    fun `trying to set progress value to number outside of default min-max range using attributes throws exception`() {
        buildView {
            mockIntAttr(R.styleable.SwipeProgressView_android_progress, 200)
        }
    }

    @Test(expected = IllegalStateException::class)
    fun `trying to set invalid min-max range using attributes throws exception`() {
        buildView {
            mockIntAttr(R.styleable.SwipeProgressView_spv_minProgress, 200)
            mockIntAttr(R.styleable.SwipeProgressView_spv_maxProgress, 100)
        }
    }

    @Test(expected = IllegalStateException::class)
    fun `trying to set progress value to number outside of selected min-max range using attributes throws exception`() {
        buildView {
            mockIntAttr(R.styleable.SwipeProgressView_spv_minProgress, 150)
            mockIntAttr(R.styleable.SwipeProgressView_spv_maxProgress, 200)
            mockIntAttr(R.styleable.SwipeProgressView_android_progress, 0)
        }
    }

    @Test
    fun `setting only min progress using attributes will also set current progress to that value`() {
        val view = buildView {
            mockIntAttr(R.styleable.SwipeProgressView_spv_minProgress, 10)
        }

        assertThat(view.progress, equalTo(10))
    }

    @Test
    fun `setting empty min-max range using attributes will work`() {
        val view = buildView {
            mockIntAttr(R.styleable.SwipeProgressView_spv_minProgress, 10)
            mockIntAttr(R.styleable.SwipeProgressView_spv_maxProgress, 10)
        }

        assertThat(view.minProgress, equalTo(10))
        assertThat(view.maxProgress, equalTo(10))
    }

    @Test
    fun `setting progress with animation changes progress value`() {
        val view = buildView()

        view.setProgressAnimated(50)

        assertThat(view.progress, equalTo(50))
    }
}

