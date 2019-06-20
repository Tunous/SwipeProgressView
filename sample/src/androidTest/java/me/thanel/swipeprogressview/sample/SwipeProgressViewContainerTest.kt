package me.thanel.swipeprogressview.sample

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.longClick
import androidx.test.espresso.action.ViewActions.swipeRight
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import kotlinx.android.synthetic.main.activity_container_test.*
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SwipeProgressViewContainerTest {

    @get:Rule
    val rule = ActivityTestRule(ContainerTestActivity::class.java)

    @Test
    fun can_click_on_child_view() {
        var clicked = false
        rule.activity.childButton.setOnClickListener {
            clicked = true
        }

        onView(withId(R.id.childButton)).perform(click())

        assertThat(clicked, equalTo(true))
    }

    @Test
    fun can_long_click_on_child_view() {
        var longClicked = false
        rule.activity.childButton.setOnLongClickListener {
            longClicked = true
            true
        }

        onView(withId(R.id.childButton)).perform(longClick())

        assertThat(longClicked, equalTo(true))
    }

    @Test
    fun swiping_on_child_button_swipes_progress_view() {
        var swiped = false
        rule.activity.swipeProgressView.setOnProgressChangeListener {
            swiped = true
        }

        onView(withId(R.id.childButton)).perform(swipeRight())

        assertThat(swiped, equalTo(true))
    }
}
