package de.vorb.sokrates.properties;

import lombok.Data;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

@Data
public class GeneratorProperties {

    private Path pandocExecutable;
    private Map<String, String> extensionMapping = new HashMap<>();

}
