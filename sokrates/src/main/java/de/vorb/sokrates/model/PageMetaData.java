package de.vorb.sokrates.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
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
    private Set<String> tags = new HashSet<>();

    private Map<String, Object> properties = new HashMap<>();

    public LocalDate getLastModifiedAt() {
        return lastModifiedAt != null ? lastModifiedAt : createdAt;
    }

    public Set<String> getTags() {
        if (tags == null) {
            return Collections.emptySet();
        } else {
            return Collections.unmodifiableSet(tags);
        }
    }

    public Map<String, Object> getProperties() {
        if (properties == null) {
            return Collections.emptyMap();
        } else {
            return Collections.unmodifiableMap(properties);
        }
    }

    public Map<String, Object> toMap() {
        final Map<String, Object> map = new HashMap<>();
        map.put("title", getTitle());
        map.put("author", getAuthor());
        map.put("createdAt", getCreatedAt());
        map.put("lastModifiedAt", getLastModifiedAt());
        map.put("tags", getTags());
        map.put("properties", getProperties());
        return map;
    }

}
