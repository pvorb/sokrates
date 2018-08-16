package de.vorb.sokrates.generator;

import de.vorb.sokrates.cli.GenerateCommand;
import de.vorb.sokrates.db.DbConfiguration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.validation.Validation;
import javax.validation.Validator;

@Configuration
@ComponentScan
@Import({DbConfiguration.class, YamlConfiguration.class})
@ConditionalOnBean(name = GenerateCommand.NAME)
public class GeneratorConfiguration {

    @Bean
    public Validator validator() {
        return Validation.buildDefaultValidatorFactory().getValidator();
    }

}
