package me.thanel.swipeprogressview.sample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_swipe.view.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        swipeItemRecyclerView.layoutManager = LinearLayoutManager(this)
        swipeItemRecyclerView.adapter = SwipeItemAdapter()
    }
}

class SwipeItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    init {
        itemView.clickableChild.setOnClickListener {
            Toast.makeText(itemView.context, "Tapped", Toast.LENGTH_SHORT).show()
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

    override fun onBindViewHolder(holder: SwipeItemViewHolder, position: Int) = Unit
}
