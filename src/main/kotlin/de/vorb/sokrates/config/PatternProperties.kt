package de.vorb.sokrates.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "pattern")
class PatternProperties {

    var newFile: String = "{slug}.md"

    var permalink: String = "{year}/{month}/{day}/{slug}.html"

}
