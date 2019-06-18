package me.thanel.swipeprogressview.sample

import android.os.Bundle
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_recycler_view_test.*

class RecyclerViewTestActivity : AppCompatActivity() {

    @VisibleForTesting
    internal val progressValues = mutableMapOf<Int, Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_view_test)

        progressValues.clear()
        swipeItemRecyclerView.layoutManager = LinearLayoutManager(this)
        swipeItemRecyclerView.adapter = SwipeItemAdapter { position, progress ->
            progressValues[position] = progress
        }
    }
}
