package jp.takuji31.kanmoba.model

enum class Status {
    INTERESTED, LIKE, LOVE;

    fun toDisplayString(): String = when (this) {
        INTERESTED -> ""
        LIKE -> "Like"
        LOVE -> "Love"
    }
}