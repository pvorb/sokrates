package de.vorb.sokrates.generator;

import de.vorb.sokrates.cli.GenerateCommand;
import de.vorb.sokrates.db.DbConfiguration;
import de.vorb.sokrates.properties.SokratesProperties;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mitchellbosecke.pebble.PebbleEngine;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.validation.Validation;
import javax.validation.Validator;

@Configuration
@ComponentScan
@Import(DbConfiguration.class)
@ConditionalOnBean(name = GenerateCommand.NAME)
public class GeneratorConfiguration {

    @Bean
    public ObjectMapper yamlObjectMapper() {
        return new ObjectMapper(new YAMLFactory())
                .registerModule(new JavaTimeModule())
                .setPropertyNamingStrategy(PropertyNamingStrategy.KEBAB_CASE);
    }

    @Bean
    public Validator validator() {
        return Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Bean
    public PebbleEngine pebbleEngine(TemplateFileLoader templateFileLoader) {
        return new PebbleEngine.Builder()
                .extension(new SokratesPebbleExtension())
                .loader(templateFileLoader)
                .build();
    }

    @Bean
    public TemplateFileLoader templateFileLoader(SokratesProperties sokratesProperties) {
        return new TemplateFileLoader(sokratesProperties.getDirectory().getTemplates());
    }

    @Bean
    public Sha1ChecksumCalculator checksumCalculator() {
        return new Sha1ChecksumCalculator();
    }

}
