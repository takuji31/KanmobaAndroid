package jp.takuji31.kanmoba.databinding

import android.databinding.BindingAdapter
import android.databinding.InverseBindingAdapter
import android.databinding.InverseBindingListener
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView


@BindingAdapter("items")
fun <T : Any?> setItems(recyclerView: RecyclerView, items : T) {
    @Suppress("UNCHECKED_CAST")
    (recyclerView.adapter as? HasItems<T>)?.run {
        this.items = items
    }
}

@InverseBindingAdapter(attribute = "isRefreshing", event = "isRefreshingChanged")
fun getSwipeRefreshLayoutRefreshing(swipeRefreshLayout: SwipeRefreshLayout) : Boolean = swipeRefreshLayout.isRefreshing

@BindingAdapter("isRefreshing")
fun setSwipeRefreshLayoutIsRefreshing(swipeRefreshLayout: SwipeRefreshLayout, value: Boolean) {
    if (swipeRefreshLayout.isRefreshing != value) {
        swipeRefreshLayout.isRefreshing = value
    }
}

@BindingAdapter("isRefreshingChanged")
fun setSwipeRefreshIsRefreshingInverseBindingListener(refreshLayout: SwipeRefreshLayout, isRefreshingChanged: InverseBindingListener) {
    refreshLayout.setOnRefreshListener {
        isRefreshingChanged.onChange()
    }
}