package jp.takuji31.kanmoba

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import jp.takuji31.kanmoba.num19.BottomNavigationViewActivity
import jp.takuji31.kanmoba.num20.ItemDecorationActivity
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

class MainActivity : AppCompatActivity() {

    val recyclerView by lazy {
        findViewById(R.id.recyclerView) as RecyclerView
    }

    enum class Kanmoba(val number : Int, val date:LocalDate, val activityClass : Class<out Activity>) {
        Num19(19, LocalDate.of(2016, 10, 26), BottomNavigationViewActivity::class.java),
        Num20(20, LocalDate.of(2016, 11, 28), ItemDecorationActivity::class.java),
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView.adapter = Adapter(items = Kanmoba.values().toList(), onItemClicked = {
            startActivity(Intent(this, it.activityClass))
        })
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    class Adapter(val items : List<Kanmoba>, val onItemClicked: (Kanmoba) -> Unit) : RecyclerView.Adapter<ViewHolder>() {
        override fun getItemCount(): Int {
            return items.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val kanmoba = items[position]
            holder.textView.text = "#${kanmoba.number} (${kanmoba.date.format(DateTimeFormatter.ISO_DATE)})"
            holder.textView.setOnClickListener {
                onItemClicked(kanmoba)
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1, parent, false) as TextView)
        }
    }

    class ViewHolder(val textView: TextView) : RecyclerView.ViewHolder(textView) {
    }
}
