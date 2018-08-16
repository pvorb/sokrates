package de.vorb.sokrates.properties;

import lombok.Data;

import java.net.URI;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Data
public class SiteProperties {

    private Locale defaultLocale = Locale.forLanguageTag("en-US");

    private String title;
    private String subtitle;

    private URI publicUrl;

    private String author;
    private URI authorUrl;

    private String translations = "src/site/resources/translations";

    private Map<String, Object> properties = new HashMap<>();

}
