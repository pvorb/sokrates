package de.vorb.sokrates.properties.converter;

import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@ConfigurationPropertiesBinding
public class LocaleConverter implements Converter<String, Locale> {

    @Override
    public Locale convert(String source) {
        return Locale.forLanguageTag(source);
    }

}
