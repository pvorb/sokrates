package de.vorb.sokrates.config

import java.util.*

class IndexProperties {

    var name: String = ""

    var itemType: String = ""

    var outputPattern: String = ""

    var template: String = ""

    var itemsPerPage: Long = Long.MAX_VALUE

    var maxItems: Long = Long.MAX_VALUE

    val groupBy: List<String> = ArrayList()

    val orderBy: List<OrderProperties> = ArrayList()

    val misc: Map<String, String> = HashMap()

}
