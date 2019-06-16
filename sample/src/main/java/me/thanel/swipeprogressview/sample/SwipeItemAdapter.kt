package me.thanel.swipeprogressview.sample

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class SwipeItemAdapter : RecyclerView.Adapter<SwipeItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SwipeItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_swipe, parent, false)
        return SwipeItemViewHolder(view)
    }

    override fun getItemCount() = 10

    override fun onBindViewHolder(holder: SwipeItemViewHolder, position: Int) {
        val progress = position * 20
        holder.textView.text = progress.toString()
        holder.swipeProgressView.progress = progress
    }
}
