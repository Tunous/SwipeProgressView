package me.thanel.swipeprogressview

import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import org.hamcrest.CoreMatchers
import org.junit.Assert

fun assertIsColorDrawableOfColor(drawable: Drawable, @ColorInt color: Int) {
    Assert.assertThat(drawable, CoreMatchers.instanceOf(ColorDrawable::class.java))
    val colorDrawable = drawable as ColorDrawable
    Assert.assertThat(colorDrawable.color, CoreMatchers.equalTo(color))
}
