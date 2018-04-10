package de.vorb.sokrates.generator.tpl.freemarker;

import de.vorb.sokrates.properties.SokratesProperties;

import freemarker.template.Configuration;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;

import java.nio.charset.StandardCharsets;

@org.springframework.context.annotation.Configuration
@RequiredArgsConstructor
public class FreemarkerConfiguration {

    private final SokratesProperties sokratesProperties;

    @Bean
    @SneakyThrows
    public Configuration freemarkerTemplateEngineConfiguration() {
        final Configuration cfg = new Configuration(Configuration.VERSION_2_3_28);
        cfg.setDirectoryForTemplateLoading(sokratesProperties.getDirectory().getTemplates().toFile());
        cfg.setDefaultEncoding(StandardCharsets.UTF_8.toString());
        cfg.setLogTemplateExceptions(true);
        return cfg;
    }

}
