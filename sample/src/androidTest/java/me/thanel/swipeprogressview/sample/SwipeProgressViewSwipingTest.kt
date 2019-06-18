package me.thanel.swipeprogressview.sample

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.GeneralLocation
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import kotlinx.android.synthetic.main.activity_basic_test.*
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SwipeProgressViewSwipingTest {

    @get:Rule
    val rule = ActivityTestRule<BasicTestActivity>(BasicTestActivity::class.java)

    @Test
    fun swiping_right_through_whole_view_increases_progress_by_50_percent() {
        onView(withId(R.id.swipeProgressView))
            .perform(swipe(GeneralLocation.CENTER_LEFT, GeneralLocation.CENTER_RIGHT))

        assertThat(rule.activity.swipeProgressView.progress, equalTo(100))
    }

    @Test
    fun swiping_right_through_half_of_view_increases_progress_by_25_percent() {

        onView(withId(R.id.swipeProgressView))
            .perform(swipe(GeneralLocation.CENTER_LEFT, GeneralLocation.CENTER))

        assertThat(rule.activity.swipeProgressView.progress, equalTo(75))
    }

    @Test
    fun swiping_left_through_whole_view_decreases_progress_by_50_percent() {
        onView(withId(R.id.swipeProgressView))
            .perform(swipe(GeneralLocation.CENTER_RIGHT, GeneralLocation.CENTER_LEFT))

        assertThat(rule.activity.swipeProgressView.progress, equalTo(0))
    }

    @Test
    fun swiping_left_through_half_of_view_decreases_progress_by_25_percent() {
        onView(withId(R.id.swipeProgressView))
            .perform(swipe(GeneralLocation.CENTER_RIGHT, GeneralLocation.CENTER))

        assertThat(rule.activity.swipeProgressView.progress, equalTo(25))
    }
}