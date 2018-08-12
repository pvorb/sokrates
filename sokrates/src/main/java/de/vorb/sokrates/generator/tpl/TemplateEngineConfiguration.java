package de.vorb.sokrates.generator.tpl;

import de.vorb.sokrates.properties.SokratesProperties;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import java.nio.charset.StandardCharsets;

@Configuration
public class TemplateEngineConfiguration {

    @Bean
    public MessageSource messageSource(SokratesProperties sokratesProperties) {
        final ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setDefaultEncoding(StandardCharsets.UTF_8.toString());
        messageSource.setBasename("file:" + sokratesProperties.getSite().getTranslations());
        messageSource.setFallbackToSystemLocale(false);
        return messageSource;
    }

}
