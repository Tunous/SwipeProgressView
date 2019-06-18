package me.thanel.swipeprogressview.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_recycler_view_test.*

class RecyclerViewTestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_view_test)

        swipeItemRecyclerView.layoutManager = LinearLayoutManager(this)
        swipeItemRecyclerView.adapter = SwipeItemAdapter()
    }
}
