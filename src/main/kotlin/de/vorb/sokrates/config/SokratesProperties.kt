package de.vorb.sokrates.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.NestedConfigurationProperty
import org.springframework.stereotype.Component
import java.util.*

@Component
@ConfigurationProperties(prefix = "sokrates")
class SokratesProperties {

    @NestedConfigurationProperty
    val website: WebsiteProperties = WebsiteProperties()

    @NestedConfigurationProperty
    val itemTypes: List<ItemTypeProperties> = ArrayList()

    @NestedConfigurationProperty
    val indexes: List<IndexProperties> = ArrayList()

    val misc: Map<String, String> = HashMap()

}
