package de.vorb.sokrates.properties;

import de.vorb.sokrates.properties.converter.LocaleConverter;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class PropertiesConfiguration {

    @Bean
    @ConfigurationPropertiesBinding
    public LocaleConverter localeConverter() {
        return new LocaleConverter();
    }

    @Bean
    @DependsOn("localeConverter")
    @ConfigurationProperties(prefix = "sokrates")
    public SokratesProperties sokratesProperties() {
        return new SokratesProperties();
    }

}
