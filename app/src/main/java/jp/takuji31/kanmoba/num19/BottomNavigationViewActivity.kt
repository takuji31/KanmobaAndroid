package jp.takuji31.kanmoba.num19

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import jp.takuji31.kanmoba.R

class BottomNavigationViewActivity : AppCompatActivity() {

    val bottomNavigationView by lazy {
        findViewById(R.id.bottomNavigationView) as BottomNavigationView
    }

    val textView by lazy {
        findViewById(R.id.textView) as TextView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bottom_navigation_view)
        bottomNavigationView.setOnNavigationItemSelectedListener {
            textView.text = it.title.toString() + " selected"
            true
        }
    }
}
