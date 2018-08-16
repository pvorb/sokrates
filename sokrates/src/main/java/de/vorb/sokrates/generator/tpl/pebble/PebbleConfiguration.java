package de.vorb.sokrates.generator.tpl.pebble;

import de.vorb.sokrates.properties.SokratesProperties;

import com.mitchellbosecke.pebble.PebbleEngine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PebbleConfiguration {

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

}
