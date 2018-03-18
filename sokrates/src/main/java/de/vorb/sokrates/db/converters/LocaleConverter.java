package de.vorb.sokrates.db.converters;

import org.jooq.Converter;

import java.util.Locale;

public class LocaleConverter implements Converter<String, Locale> {

    @Override
    public Locale from(String locale) {
        return locale == null ? null : Locale.forLanguageTag(locale);
    }

    @Override
    public String to(Locale locale) {
        return locale == null ? null : locale.toLanguageTag();
    }

    @Override
    public Class<String> fromType() {
        return String.class;
    }

    @Override
    public Class<Locale> toType() {
        return Locale.class;
    }

}
