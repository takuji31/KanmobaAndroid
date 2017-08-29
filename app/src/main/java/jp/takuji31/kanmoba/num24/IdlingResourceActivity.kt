package jp.takuji31.kanmoba.num24

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.annotation.VisibleForTesting
import android.support.v7.app.AppCompatActivity
import android.support.v7.util.DiffUtil
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.TextView
import jp.takuji31.kanmoba.R
import jp.takuji31.kanmoba.databinding.ActivityIdlingResourceBinding
import jp.takuji31.kanmoba.databinding.HasItems
import jp.takuji31.kanmoba.model.Artist
import jp.takuji31.kanmoba.model.Status
import jp.takuji31.kanmoba.model.Status.*
import kotlin.properties.Delegates

/**
 * from https://github.com/takuji31/DiffUtilSample
 */
class IdlingResourceActivity : AppCompatActivity() {

    val adapter by lazy {
        Adapter()
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val viewModel : IdlingResourceViewModel by lazy {
        IdlingResourceViewModel()
    }

    var binding: ActivityIdlingResourceBinding by Delegates.notNull()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_idling_resource)

        val recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        binding.viewModel = viewModel
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_sort_by_name -> {
                val newList = adapter.items.sortedBy { it.name }
                updateItems(items = newList)
            }
            R.id.action_sort_by_status -> {
                val newList = adapter.items.sortedByDescending { it.status }
                updateItems(items = newList)
            }
            R.id.action_reset -> {
                val newList = Artist.list
                updateItems(items = newList)
            }
            R.id.action_remove_first -> {
                val newList = adapter.items.drop(1)
                updateItems(items = newList)
            }
            R.id.action_remove_last -> {
                val newList = adapter.items.dropLast(1)
                updateItems(items = newList)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun updateItems(items : List<Artist>) {
        val oldItems = adapter.items
        val diffResult = DiffUtil.calculateDiff(DiffCallback(oldList = oldItems, newList = items), true)
        adapter.items = items
        diffResult.dispatchUpdatesTo(adapter)
    }

    class DiffCallback(val oldList: List<Artist>, val newList: List<Artist>) : DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].name == newList[newItemPosition].name
        }

        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }

        override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Pair<Status, Status> {
            return Pair(oldList[oldItemPosition].status, newList[newItemPosition].status)
        }
    }

    class Adapter() : RecyclerView.Adapter<Adapter.ViewHolder>(), HasItems<List<Artist>> {

        override var items = listOf<Artist>()

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = items[position]

            holder.nameTextView.text = item.name
            holder.statusTextView.text = item.status.toDisplayString()
            holder.itemView.setOnClickListener {
                val adapterPosition = holder.adapterPosition
                val oldItem = items[adapterPosition]
                val newStatus = when (oldItem.status) {
                    INTERESTED -> LIKE
                    LIKE -> LOVE
                    LOVE -> LOVE
                }
                val newItem = oldItem.copy(status = newStatus)
                val newItems = items.take(adapterPosition) + newItem + items.drop(adapterPosition + 1)
                val diffResult = DiffUtil.calculateDiff(DiffCallback(items, newItems))
                items = newItems
                diffResult.dispatchUpdatesTo(this)
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            return ViewHolder(inflater.inflate(R.layout.recycler_view_row, parent, false))
        }

        override fun getItemCount(): Int = items.count()

        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val nameTextView by lazy {
                itemView.findViewById(R.id.nameTextView) as TextView
            }
            val statusTextView by lazy {
                itemView.findViewById(R.id.statusTextView) as TextView
            }
        }
    }

}