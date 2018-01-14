package de.vorb.sokrates.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "sokrates")
public class SokratesProperties {

    private SiteProperties site;
    private DirectoryProperties directory;
    private ParserProperties parser;
    private GeneratorProperties generator;

    private List<IndexProperties> indexes = new ArrayList<>();

}
