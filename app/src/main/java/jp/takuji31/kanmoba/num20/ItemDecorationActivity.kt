package jp.takuji31.kanmoba.num20

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.os.Bundle
import android.support.annotation.DrawableRes
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import jp.takuji31.kanmoba.R

class ItemDecorationActivity : AppCompatActivity() {

    enum class AccessoryType(@DrawableRes val resId: Int) {
        NONE(0),
        DISCLOSURE_INDICATOR(R.drawable.ic_chevron_right_black_24dp),
        CHECK_MARK(R.drawable.ic_done_black_24dp),
    }

    val recyclerView by lazy {
        findViewById(R.id.recyclerView) as RecyclerView
    }

    class Adapter(var items: List<Pair<String, AccessoryType>>) : RecyclerView.Adapter<ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val inflator = LayoutInflater.from(parent.context)
            return ViewHolder(view = inflator.inflate(R.layout.recyler_row_simple_textview, parent, false))
        }

        override fun getItemCount(): Int {
            return items.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val (text, accessoryType) = items[position]
            holder.textView.text = text
            holder.textView.setOnClickListener {
                Log.d(javaClass.simpleName, "clicked")
            }
            holder.itemView.setTag(R.id.tagAccessoryType, accessoryType)
        }
    }

    class AccessoryItemDecoration(private val context: Context) : RecyclerView.ItemDecoration() {
        val horizontalMargin by lazy {
            context.resources.getDimensionPixelOffset(R.dimen.accessory_horizontal_margin)
        }

        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            val accessoryType = view.getTag(R.id.tagAccessoryType) as? AccessoryType
            if (accessoryType != null) {
                if (accessoryType != AccessoryType.NONE) {
                    val drawable = ContextCompat.getDrawable(view.context, accessoryType.resId)
                    outRect.right = drawable.intrinsicWidth + view.context.resources.getDimensionPixelOffset(R.dimen.accessory_horizontal_margin) * 2
                }
            }
        }

        override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        }

        override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {

            val childCount = parent.childCount
            for (i in 0..childCount - 1) {
                val child = parent.getChildAt(i)
                val accessoryType = child.getTag(R.id.tagAccessoryType) as? AccessoryType
                if (accessoryType != null && accessoryType != AccessoryType.NONE) {
                    val drawable = ContextCompat.getDrawable(child.context, accessoryType.resId)
                    val bounds = Rect()
                    parent.getDecoratedBoundsWithMargins(child, bounds)
                    val left = bounds.right - horizontalMargin - drawable.intrinsicWidth
                    val right = left + drawable.intrinsicWidth
                    val top = bounds.top + ((bounds.bottom - bounds.top - drawable.intrinsicHeight) / 2) + Math.round(ViewCompat.getTranslationY(child))
                    val bottom = top + drawable.intrinsicHeight
                    drawable.setBounds(left, top, right, bottom)
                    drawable.draw(c)
                }
            }
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView by lazy {
            view as TextView
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_decoration)
        recyclerView.adapter = Adapter(listOf(Pair("foo", AccessoryType.NONE), Pair("bar", AccessoryType.DISCLOSURE_INDICATOR), Pair("baz", AccessoryType.CHECK_MARK)))
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(AccessoryItemDecoration(context = this))
    }
}
