package me.thanel.swipeprogressview.sample

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        basicButton.setOnClickListener {
            startActivity(Intent(this, BasicTestActivity::class.java))
        }
        recyclerViewButton.setOnClickListener {
            startActivity(Intent(this, RecyclerViewTestActivity::class.java))
        }
    }
}
