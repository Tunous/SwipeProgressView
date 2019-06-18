package me.thanel.swipeprogressview.sample

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.longClick
import androidx.test.espresso.action.ViewActions.swipeLeft
import androidx.test.espresso.action.ViewActions.swipeRight
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SwipeProgressViewClickingTest {

    @get:Rule
    val rule = ActivityTestRule<BasicTestActivity>(BasicTestActivity::class.java)

    @Test
    fun clicking_on_swipe_progress_view_invokes_click_listener() {
        onView(withId(R.id.swipeProgressView)).perform(click())

        assertThat(rule.activity.clickCount, equalTo(1))
    }

    @Test
    fun swiping_swipe_progress_view_does_not_call_click_listener() {
        onView(withId(R.id.swipeProgressView)).perform(swipeLeft())

        assertThat(rule.activity.clickCount, equalTo(0))
    }

    @Test
    fun long_clicking_on_swipe_progress_view_invokes_long_click_listener() {
        onView(withId(R.id.swipeProgressView)).perform(longClick())

        assertThat(rule.activity.longClickCount, equalTo(1))
    }

    @Test
    fun swiping_swipe_progress_view_does_not_call_long_click_listener() {
        onView(withId(R.id.swipeProgressView)).perform(swipeRight())

        assertThat(rule.activity.longClickCount, equalTo(0))
    }

    @Test
    fun clicking_on_swipe_progress_view_does_not_call_long_click_listener() {
        onView(withId(R.id.swipeProgressView)).perform(click())

        assertThat(rule.activity.longClickCount, equalTo(0))
    }

    @Test
    fun long_clicking_on_swipe_progress_view_does_not_call_click_listener() {
        onView(withId(R.id.swipeProgressView)).perform(longClick())

        assertThat(rule.activity.clickCount, equalTo(0))
    }
}
