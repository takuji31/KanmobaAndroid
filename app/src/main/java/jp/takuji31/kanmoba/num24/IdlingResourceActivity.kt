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
        recyclerView.adapter = Adapter()

        binding.viewModel = viewModel
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_sort_by_name -> {
                viewModel.sortByName()
            }
            R.id.action_sort_by_status -> {
                viewModel.sortByStatus()
            }
            R.id.action_reset -> {
                viewModel.reset()
            }
            R.id.action_remove_first -> {
                viewModel.removeFirst()
            }
            R.id.action_remove_last -> {
                viewModel.removeLast()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    class DiffCallback(val oldList: List<Artist>, val newList: List<Artist>) : DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean = oldList[oldItemPosition].name == newList[newItemPosition].name

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean = oldList[oldItemPosition] == newList[newItemPosition]

        override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Pair<Status, Status> = Pair(oldList[oldItemPosition].status, newList[newItemPosition].status)
    }

    class Adapter : RecyclerView.Adapter<Adapter.ViewHolder>(), HasItems<List<Artist>> {

        override var items = listOf<Artist>()
            set(value) {
                val oldItems = field
                val diffResult = DiffUtil.calculateDiff(DiffCallback(oldList = oldItems, newList = value), true)
                field = value
                diffResult.dispatchUpdatesTo(this)
            }

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

                items = items.take(adapterPosition) + oldItem.copy(status = newStatus) + items.drop(adapterPosition + 1)
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            return ViewHolder(inflater.inflate(R.layout.recycler_view_row, parent, false))
        }

        override fun getItemCount(): Int = items.size

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