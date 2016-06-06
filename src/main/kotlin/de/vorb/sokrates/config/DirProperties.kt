package de.vorb.sokrates.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "dir")
class DirProperties {

    var source: String = "src"

    var posts: String = "src/_posts"

    var pages: String = "src/_pages"

    var public: String = "public"

}
