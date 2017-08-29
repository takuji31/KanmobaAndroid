package jp.takuji31.kanmoba.num24

import android.databinding.BaseObservable
import android.databinding.Bindable
import android.support.annotation.VisibleForTesting
import com.android.databinding.library.baseAdapters.BR
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import jp.takuji31.kanmoba.model.Artist
import java.util.concurrent.TimeUnit

class IdlingResourceViewModel : BaseObservable() {

    var loading : Boolean = false
        @Bindable get
        set(value) {
            val oldValue = field
            field = value
            if (oldValue != value) {
                notifyPropertyChanged(BR.loading)
                if (value) {
                    fakeReload()
                }
            }
        }

    var artists: List<Artist> = emptyList()
        @Bindable get
        private set(value) {
            field = value
            notifyPropertyChanged(BR.artists)
        }

    var loadingDisposable: Disposable? = null

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val loadingIdlingResource : SimpleIdlingResource = SimpleIdlingResource("IdlingResourceViewModelLoading")

    fun onCreate() {
        // fake API request
        fakeReload()
    }

    private fun fakeReload() {
        loadingDisposable = Single
                .timer(3, TimeUnit.SECONDS)
                .doOnSubscribe {
                    loading = true
                    loadingIdlingResource.isIdle = false
                }
                .doFinally {
                    loading = false
                    loadingIdlingResource.isIdle = true
                }
                .subscribe { _, _ ->
                    artists = Artist.list
                }
    }

    fun onDestroy() {
        loadingDisposable?.run {
            dispose()
            loadingDisposable = null
        }
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