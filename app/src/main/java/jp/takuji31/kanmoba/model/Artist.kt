package jp.takuji31.kanmoba.model

import jp.takuji31.kanmoba.model.Status.*

data class Artist(val name: String, val status: Status) {
    companion object {
        val list: List<Artist> = listOf(
                Artist(name = "小倉唯", status = LOVE),
                Artist(name = "佐倉綾音", status = LOVE),
                Artist(name = "雨宮天", status = LOVE),
                Artist(name = "水瀬いのり", status = LOVE),
                Artist(name = "悠木碧", status = LIKE),
                Artist(name = "麻倉もも", status = LIKE),
                Artist(name = "Trysail", status = LIKE),
                Artist(name = "Minami", status = LIKE),
                Artist(name = "田村ゆかり", status = INTERESTED),
                Artist(name = "水樹奈々", status = INTERESTED)
        )
    }
}