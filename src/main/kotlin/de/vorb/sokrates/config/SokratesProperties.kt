package de.vorb.sokrates.config

import com.beust.jcommander.JCommander
import freemarker.cache.ClassTemplateLoader
import freemarker.cache.FileTemplateLoader
import freemarker.cache.MultiTemplateLoader
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.NestedConfigurationProperty
import org.springframework.context.annotation.Bean
import java.nio.file.Paths
import java.util.*

@ConfigurationProperties(prefix = "sokrates")
open class SokratesProperties {

    @NestedConfigurationProperty
    val website: WebsiteProperties = WebsiteProperties()

    var themesDir: String = ""

    var fileTemplatesDir: String = ""

    @NestedConfigurationProperty
    val itemTypes: List<ItemTypeProperties> = ArrayList()

    @NestedConfigurationProperty
    val indexes: List<IndexProperties> = ArrayList()

    val misc: Map<String, String> = HashMap()


    @Bean
    open fun jCommander(): JCommander {
        val jc = JCommander()
        jc.setProgramName("sokrates")
        return jc
    }

    @Bean
    open fun fileTemplatesConfig(): freemarker.template.Configuration {

        val templateConfig = freemarker.template.Configuration(freemarker.template.Configuration.VERSION_2_3_23)

        val fileTemplateLoader = FileTemplateLoader(Paths.get(fileTemplatesDir).toFile())
        val fallbackClassTemplateLoader = ClassTemplateLoader(this.javaClass, "/templates")
        val combinedTemplateLoader = MultiTemplateLoader(arrayOf(fileTemplateLoader, fallbackClassTemplateLoader))
        templateConfig.templateLoader = combinedTemplateLoader

        templateConfig.defaultEncoding = "UTF-8"
        templateConfig.templateExceptionHandler = freemarker.template.TemplateExceptionHandler.RETHROW_HANDLER

        return templateConfig
    }

    @Bean
    open fun themesConfig(): freemarker.template.Configuration {

        val templateConfig = freemarker.template.Configuration(freemarker.template.Configuration.VERSION_2_3_23)

        templateConfig.setDirectoryForTemplateLoading(Paths.get(themesDir).toFile())
        templateConfig.defaultEncoding = "UTF-8"
        templateConfig.templateExceptionHandler = freemarker.template.TemplateExceptionHandler.RETHROW_HANDLER

        return templateConfig
    }

}
