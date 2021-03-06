package me.thanel.swipeprogressview.sample

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_swipe.view.*
import me.thanel.swipeprogressview.SwipeProgressView
import me.thanel.swipeprogressview.progressRange
import me.thanel.swipeprogressview.setProgressAnimated

class SwipeItemViewHolder(
    itemView: View,
    private val onProgressChange: (Int) -> Unit
) : RecyclerView.ViewHolder(itemView) {
    val textView: TextView = itemView.progressTextView
    val swipeProgressView: SwipeProgressView = itemView.swipeProgressView

    init {
        textView.setOnClickListener {
            val randomProgress = swipeProgressView.progressRange.random()
            swipeProgressView.setProgressAnimated(randomProgress)
        }
        swipeProgressView.setOnProgressChangeListener {
            textView.text = it.toString()
            onProgressChange(it)
        }
    }
}
