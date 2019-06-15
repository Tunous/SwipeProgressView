package me.thanel.swipeprogressview.sample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_swipe.view.*
import me.thanel.swipeprogressview.SwipeProgressView
import me.thanel.swipeprogressview.progressRange

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        swipeItemRecyclerView.layoutManager = LinearLayoutManager(this)
        swipeItemRecyclerView.adapter = SwipeItemAdapter()
    }
}

class SwipeItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val textView: TextView = itemView.progressTextView
    val swipeProgressView: SwipeProgressView = itemView.swipeProgressView

    init {
        textView.setOnClickListener {
            val randomProgress = swipeProgressView.progressRange.random()
            swipeProgressView.setCurrentProgressAnimated(randomProgress)
        }
        swipeProgressView.setOnProgressChangeListener {
            textView.text = it.toString()
        }
    }
}

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
        holder.swipeProgressView.currentProgress = progress
    }
}
