package de.vorb.sokrates.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import java.util.*

@Component
@ConfigurationProperties(prefix = "source")
class ItemTypeProperties {

    var name: String = ""

    val sourcePatterns: List<String> = ArrayList()

    var outputPattern: String = ""

}
