package de.vorb.sokrates.config

import com.beust.jcommander.JCommander
import freemarker.cache.ClassTemplateLoader
import freemarker.cache.FileTemplateLoader
import freemarker.cache.MultiTemplateLoader
import freemarker.template.TemplateExceptionHandler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.nio.file.Paths
import freemarker.template.Configuration as FreemarkerConfiguration

@Configuration
open class CliConfig @Autowired constructor(
        private val sokratesProperties: SokratesProperties) {

    @Bean
    open fun jCommander(): JCommander {
        val jc = JCommander()
        jc.setProgramName("sokrates")
        return jc
    }

    @Bean
    open fun fileTemplatesConfig(): FreemarkerConfiguration {

        val templateConfig = FreemarkerConfiguration(FreemarkerConfiguration.VERSION_2_3_23)

        val fileTemplateLoader = FileTemplateLoader(Paths.get(sokratesProperties.fileTemplatesDir).toFile())
        val fallbackClassTemplateLoader = ClassTemplateLoader(this.javaClass, "/templates")
        val combinedTemplateLoader = MultiTemplateLoader(arrayOf(fileTemplateLoader, fallbackClassTemplateLoader))
        templateConfig.templateLoader = combinedTemplateLoader

        templateConfig.defaultEncoding = "UTF-8"
        templateConfig.templateExceptionHandler = TemplateExceptionHandler.RETHROW_HANDLER

        return templateConfig
    }

    @Bean
    open fun themesConfig(): FreemarkerConfiguration {

        val templateConfig = FreemarkerConfiguration(FreemarkerConfiguration.VERSION_2_3_23)

        templateConfig.setDirectoryForTemplateLoading(Paths.get(sokratesProperties.themesDir).toFile())
        templateConfig.defaultEncoding = "UTF-8"
        templateConfig.templateExceptionHandler = TemplateExceptionHandler.RETHROW_HANDLER

        return templateConfig
    }

}
