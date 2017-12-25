package de.vorb.sokrates.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.nio.file.Path;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Data
public class PageDescriptor {

    @JsonIgnore
    private Path descriptorFile;

    private final Path contentFile;

    @NotNull
    @NotEmpty
    private final String title;

    private final Instant createdAt;
    private final Instant lastModifiedAt;

    @Getter(onMethod = @__({@JsonAnyGetter}))
    private final Map<String, Object> additionalProperties = new HashMap<>();

    @JsonAnySetter
    void setAdditionalProperty(String key, Object value) {
        additionalProperties.put(key, value);
    }
}
