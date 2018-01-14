package de.vorb.sokrates.properties;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Data
@Component
public class IndexProperties {

    private String name;
    private Path template;
    private Path outputFile;

    private List<String> orderBy = new ArrayList<>();
    private String where;
    private Long limit;

    private Properties properties;

}
