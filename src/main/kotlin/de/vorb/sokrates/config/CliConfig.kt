package de.vorb.sokrates.config

import com.beust.jcommander.JCommander
import freemarker.template.TemplateExceptionHandler
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import freemarker.template.Configuration as FreemarkerConfiguration

@Configuration
@EnableConfigurationProperties(WebsiteProperties::class, DirProperties::class)
open class CliConfig {

    @Bean
    open fun jCommander(): JCommander {
        val jc = JCommander()
        jc.setProgramName("sokrates")
        return jc
    }

    @Bean
    open fun fileTemplatesConfig(): FreemarkerConfiguration {
        val tplConfig = FreemarkerConfiguration(FreemarkerConfiguration.VERSION_2_3_23)
        tplConfig.setClassForTemplateLoading(this.javaClass, "/file-templates")
        tplConfig.defaultEncoding = "UTF-8"
        tplConfig.templateExceptionHandler = TemplateExceptionHandler.RETHROW_HANDLER
        return tplConfig
    }

}
