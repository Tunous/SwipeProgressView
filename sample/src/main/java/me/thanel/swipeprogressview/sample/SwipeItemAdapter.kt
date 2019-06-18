package me.thanel.swipeprogressview.sample

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class SwipeItemAdapter(
    private val onProgressChange: (Int, Int) -> Unit
) : RecyclerView.Adapter<SwipeItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SwipeItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_swipe, parent, false)
        return SwipeItemViewHolder(view) { progress ->
            val position = view.tag as Int
            onProgressChange(position, progress)
        }
    }

    override fun getItemCount() = 10

    override fun onBindViewHolder(holder: SwipeItemViewHolder, position: Int) {
        holder.itemView.tag = position
        val progress = position * 2
        holder.textView.text = progress.toString()
        holder.swipeProgressView.progress = progress
    }
}
