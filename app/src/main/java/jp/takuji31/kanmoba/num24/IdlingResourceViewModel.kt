package jp.takuji31.kanmoba.num24

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.android.databinding.library.baseAdapters.BR
import jp.takuji31.kanmoba.model.Artist

class IdlingResourceViewModel : BaseObservable() {

    var artists: List<Artist> = Artist.list
        @Bindable get
        private set(value) {
            field = value
            notifyPropertyChanged(BR.artists)
        }

    fun sortByName() {
        artists = artists.sortedBy { it.name }
    }

    fun sortByStatus() {
        artists = artists.sortedByDescending { it.status }
    }

    fun reset() {
        artists = Artist.list
    }

    fun removeFirst() {
        artists = artists.drop(1)
    }

    fun removeLast() {
        artists = artists.dropLast(1)
    }

}