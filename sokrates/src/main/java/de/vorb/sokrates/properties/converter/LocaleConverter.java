package de.vorb.sokrates.properties.converter;

import org.springframework.core.convert.converter.Converter;

import java.util.Locale;

public class LocaleConverter implements Converter<String, Locale> {

    @Override
    public Locale convert(String source) {
        return Locale.forLanguageTag(source);
    }

}
