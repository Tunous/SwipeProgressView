package me.thanel.swipeprogressview.sample

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_basic_test.*

class BasicTestActivity : AppCompatActivity() {

    internal var clickCount = 0
        private set

    internal var longClickCount = 0
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_basic_test)
        clickCount = 0
        swipeProgressView.setOnClickListener {
            clickCount += 1
            Toast.makeText(this, "Clicked nr $clickCount", Toast.LENGTH_SHORT).show()
        }
        longClickCount = 0
        swipeProgressView.setOnLongClickListener {
            longClickCount += 1
            Toast.makeText(this, "Long clicked nr $longClickCount", Toast.LENGTH_SHORT).show()
            true
        }
    }
}
