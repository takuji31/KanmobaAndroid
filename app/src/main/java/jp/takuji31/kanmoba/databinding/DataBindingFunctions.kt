package jp.takuji31.kanmoba.databinding

import android.databinding.BindingAdapter
import android.support.v7.widget.RecyclerView


@BindingAdapter("items")
fun <T : Any?> setItems(recyclerView: RecyclerView, items : T) {
    @Suppress("UNCHECKED_CAST")
    (recyclerView.adapter as? HasItems<T>)?.run {
        this.items = items
    }
}