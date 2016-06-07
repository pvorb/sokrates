package de.vorb.sokrates.config

import com.beust.jcommander.JCommander
import freemarker.cache.ClassTemplateLoader
import freemarker.cache.FileTemplateLoader
import freemarker.cache.MultiTemplateLoader
import freemarker.template.TemplateExceptionHandler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.nio.file.Files
import java.nio.file.Paths
import freemarker.template.Configuration as FreemarkerConfiguration

@Configuration
open class CliConfig {

    @Autowired
    private var sokratesProperties: SokratesProperties = SokratesProperties()

    @Bean
    open fun jCommander(): JCommander {
        val jc = JCommander()
        jc.setProgramName("sokrates")
        return jc
    }

    @Bean
    open fun fileTemplatesConfig(): FreemarkerConfiguration {
        val templateConfig = FreemarkerConfiguration(FreemarkerConfiguration.VERSION_2_3_23)

        val fileTemplatesDir = Paths.get(sokratesProperties.fileTemplatesDir)
        val classTemplateLoader = ClassTemplateLoader(this.javaClass, "/templates")
        val backingTemplateLoaders =
                if (Files.isDirectory(fileTemplatesDir) && Files.isReadable(fileTemplatesDir)) {
                    val fileTemplateLoader = FileTemplateLoader(fileTemplatesDir.toFile())
                    arrayOf(fileTemplateLoader, classTemplateLoader)
                } else {
                    arrayOf(classTemplateLoader)
                }

        templateConfig.templateLoader = MultiTemplateLoader(backingTemplateLoaders)

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
