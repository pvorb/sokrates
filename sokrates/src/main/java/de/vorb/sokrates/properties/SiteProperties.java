package de.vorb.sokrates.properties;

import lombok.Data;

import java.util.Locale;
import java.util.Properties;

@Data
public class SiteProperties {

    private Locale defaultLocale = Locale.forLanguageTag("en-US");

    private String title;
    private String subtitle;

    private Properties properties = new Properties();

}
