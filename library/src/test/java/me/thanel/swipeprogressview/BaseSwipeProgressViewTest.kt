package me.thanel.swipeprogressview

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import androidx.test.core.app.ApplicationProvider
import com.nhaarman.mockitokotlin2.KStubbing
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.anyOrNull
import com.nhaarman.mockitokotlin2.doAnswer
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.spy
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
abstract class BaseSwipeProgressViewTest {

    protected fun buildView(
        mockAttributes: KStubbing<TypedArray>.(TypedArray) -> Unit = {}
    ): SwipeProgressView {
        val typedArray = mock<TypedArray> { typedArray ->
            on { getInt(any(), any()) } doAnswer { it.getArgument(1) }
            mockAttributes(typedArray)
        }
        val context = spy(ApplicationProvider.getApplicationContext<Context>()) {
            on {
                obtainStyledAttributes(
                    anyOrNull<AttributeSet>(),
                    eq(R.styleable.SwipeProgressView)
                )
            } doReturn typedArray
        }

        return SwipeProgressView(context)
    }
}
