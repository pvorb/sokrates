package de.vorb.sokrates.generator.tpl.freemarker;

import de.vorb.sokrates.properties.SokratesProperties;

import freemarker.cache.FileTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;

import java.nio.charset.StandardCharsets;
import java.util.Locale;

@org.springframework.context.annotation.Configuration
@RequiredArgsConstructor
public class FreemarkerConfiguration {

    private final SokratesProperties sokratesProperties;

    @Bean
    @SneakyThrows
    public Configuration freemarkerTemplateEngineConfiguration(MessageSource messageSource) {
        final Configuration cfg = new Configuration(Configuration.VERSION_2_3_28);
        cfg.setTemplateLoader(createTemplateLoader());
        cfg.setDefaultEncoding(StandardCharsets.UTF_8.toString());
        cfg.setLogTemplateExceptions(true);
        cfg.setSharedVariable("l10n", new Translations(messageSource));
        return cfg;
    }

    @SneakyThrows
    private TemplateLoader createTemplateLoader() {
        return new FileTemplateLoader(sokratesProperties.getDirectory().getTemplates().toFile());
    }

    @RequiredArgsConstructor
    public static class Translations {

        private static final Object[] NO_ARGS = new Object[0];

        private final MessageSource messageSource;

        public String translate(String key, String locale) {
            return messageSource.getMessage(key, NO_ARGS, new Locale(locale));
        }

        public String translate(String key, Object[] args, String locale) {
            return messageSource.getMessage(key, args, new Locale(locale));
        }

    }

}
