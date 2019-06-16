package me.thanel.swipeprogressview

import android.content.res.TypedArray
import com.nhaarman.mockitokotlin2.KStubbing
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doAnswer
import com.nhaarman.mockitokotlin2.eq

internal fun KStubbing<TypedArray>.mockIntAttr(index: Int, result: Int?) {
    on { getInt(eq(index), any()) } doAnswer { result ?: it.getArgument(1) }
}
