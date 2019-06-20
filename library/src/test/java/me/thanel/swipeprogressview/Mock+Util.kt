package me.thanel.swipeprogressview

import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import com.nhaarman.mockitokotlin2.KStubbing
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doAnswer
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.eq

internal fun KStubbing<TypedArray>.mockIntAttr(index: Int, result: Int?) {
    on { getInt(eq(index), any()) } doAnswer { result ?: it.getArgument(1) }
}

internal fun KStubbing<TypedArray>.mockDrawableAttr(index: Int, result: Drawable?) {
    on { getDrawable(index) } doReturn result
}
