package me.thanel.swipeprogressview.sample

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.GeneralLocation
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SwipeProgressViewScrollingTest {

    @get:Rule
    val rule = ActivityTestRule<RecyclerViewTestActivity>(RecyclerViewTestActivity::class.java)

    @Test
    fun can_scroll_on_first_swipe_progress_view_in_recycler_view() {
        onView(withId(R.id.swipeItemRecyclerView))
            .perform(
                actionOnItemAtPosition<SwipeItemViewHolder>(
                    0,
                    swipe(GeneralLocation.CENTER_LEFT, GeneralLocation.CENTER_RIGHT)
                )
            )

        assertThat(rule.activity.progressValues[0], equalTo(50))
    }

    @Test
    fun can_scroll_on_last_swipe_progress_view_in_recycler_view() {
        onView(withId(R.id.swipeItemRecyclerView))
            .perform(
                actionOnItemAtPosition<SwipeItemViewHolder>(
                    9,
                    swipe(GeneralLocation.CENTER_RIGHT, GeneralLocation.CENTER_LEFT)
                )
            )

        assertThat(rule.activity.progressValues[9], equalTo(-25))
    }

}
