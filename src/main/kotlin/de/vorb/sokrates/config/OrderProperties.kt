package de.vorb.sokrates.config

class OrderProperties {

    var field: String = ""

    var order: String = ""

    fun isAscending(): Boolean =
            if (order == "ASC") true
            else if (order == "DESC") false
            else throw IllegalArgumentException("order must be either ASC or DESC")

}
