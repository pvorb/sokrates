package de.vorb.sokrates.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class PageMetaData {

    private String title;

    private Path alias;

    private String author;
    private LocalDate createdAt;
    private LocalDate lastModifiedAt;

    private String template;
    private Locale locale;
    private Set<String> tags;

    private Map<String, Object> properties;

    public LocalDate getLastModifiedAt() {
        return lastModifiedAt != null ? lastModifiedAt : createdAt;
    }

    public Set<String> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    public Map<String, Object> getProperties() {
        return Collections.unmodifiableMap(properties);
    }

    public Map<String, Object> toMap() {
        final Map<String, Object> map = new HashMap<>();
        if (title != null) {
            map.put("title", getTitle());
        }
        if (author != null) {
            map.put("author", getAuthor());
        }
        if (createdAt != null) {
            map.put("createdAt", getCreatedAt());
        }
        if (lastModifiedAt != null) {
            map.put("lastModifiedAt", getLastModifiedAt());
        }
        if (tags != null) {
            map.put("tags", getTags());
        }
        if (properties != null) {
            map.put("properties", getProperties());
        }
        return map;
    }

}
