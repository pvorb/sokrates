package de.vorb.sokrates.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@Data
public class SokratesProperties {

    private SiteProperties site;
    private DirectoryProperties directory;
    private GeneratorProperties generator;

    private List<IndexProperties> indexes = new ArrayList<>();

}
