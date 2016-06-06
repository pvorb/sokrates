package de.vorb.sokrates.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties
class WebsiteProperties {

    var title: String = "Blog title"

    var subtitle: String = ""

    var description: String = "An insignificant blog"

    var author: String = "Jane Doe"

    var language: String = "en"

    var timezone: String = "UTC"

}
