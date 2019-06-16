package me.thanel.swipeprogressview.sample

import androidx.annotation.LayoutRes
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.GeneralLocation
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.android.synthetic.main.item_swipe.*
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SwipeProgressViewTest {

    // test_view layout has SwipeProgressView with:
    //  - min = 0
    //  - max = 100
    //  - progress = 50

    @Test
    fun swiping_right_through_whole_view_increases_progress_by_50_percent() {
        val scenario = createScenario(R.layout.test_view)

        onView(withId(R.id.swipeProgressView))
            .perform(swipe(GeneralLocation.CENTER_LEFT, GeneralLocation.CENTER_RIGHT))

        scenario.onActivity {
            assertThat(it.swipeProgressView.progress, equalTo(100))
        }
    }

    @Test
    fun swiping_right_through_half_of_view_increases_progress_by_25_percent() {
        val scenario = createScenario(R.layout.test_view)

        onView(withId(R.id.swipeProgressView))
            .perform(swipe(GeneralLocation.CENTER_LEFT, GeneralLocation.CENTER))

        scenario.onActivity {
            assertThat(it.swipeProgressView.progress, equalTo(75))
        }
    }

    @Test
    fun swiping_left_through_whole_view_decreases_progress_by_50_percent() {
        val scenario = createScenario(R.layout.test_view)

        onView(withId(R.id.swipeProgressView))
            .perform(swipe(GeneralLocation.CENTER_RIGHT, GeneralLocation.CENTER_LEFT))

        scenario.onActivity {
            assertThat(it.swipeProgressView.progress, equalTo(0))
        }
    }

    @Test
    fun swiping_left_through_half_of_view_decreases_progress_by_25_percent() {
        val scenario = createScenario(R.layout.test_view)

        onView(withId(R.id.swipeProgressView))
            .perform(swipe(GeneralLocation.CENTER_RIGHT, GeneralLocation.CENTER))

        scenario.onActivity {
            assertThat(it.swipeProgressView.progress, equalTo(25))
        }
    }

    private fun createScenario(@LayoutRes layoutResId: Int): ActivityScenario<MockActivity> {
        MockActivity.layoutResId = layoutResId
        return ActivityScenario.launch(MockActivity::class.java)
    }
}
