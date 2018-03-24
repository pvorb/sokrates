package de.vorb.sokrates.db.converters;

import org.jooq.Converter;

import java.net.URI;

public class UrlConverter implements Converter<String, URI> {

    @Override
    public URI from(String url) {
        return url == null ? null : URI.create(url);
    }

    @Override
    public String to(URI url) {
        return url == null ? null : url.toString();
    }

    @Override
    public Class<String> fromType() {
        return String.class;
    }

    @Override
    public Class<URI> toType() {
        return URI.class;
    }

}
