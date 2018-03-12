package de.vorb.sokrates.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

@Data
public class PageMetaData {

    private String title;

    private String alias;

    private LocalDate createdAt;
    private LocalDate lastModifiedAt;

    private String template;
    private Locale locale;
    private List<String> tags;

    private Properties properties;

    public Map<String, Object> toMap() {
        final Map<String, Object> map = new HashMap<>();
        if (title != null) {
            map.put("title", title);
        }
        if (createdAt != null) {
            map.put("createdAt", createdAt);
        }
        if (lastModifiedAt != null) {
            map.put("lastModifiedAt", lastModifiedAt);
        }
        if (tags != null) {
            map.put("tags", Collections.unmodifiableList(tags));
        }
        return map;
    }

}
