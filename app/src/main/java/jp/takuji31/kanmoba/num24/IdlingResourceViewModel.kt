package jp.takuji31.kanmoba.num24

import android.databinding.BaseObservable
import android.databinding.Bindable
import com.android.databinding.library.baseAdapters.BR
import jp.takuji31.kanmoba.model.Artist

class IdlingResourceViewModel : BaseObservable() {

    var artists : List<Artist> = Artist.list
        @Bindable get
        private set(value) {
            field = value
            notifyPropertyChanged(BR.artists)
        }

}