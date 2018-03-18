package de.vorb.sokrates.properties;

import lombok.Data;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class GeneratorProperties {

    private Path pandocExecutable;
    private Map<String, String> extensionMapping = new HashMap<>();
    private List<GenerateRuleProperties> generateRules = new ArrayList<>();
    private List<CopyRuleProperties> copyRules = new ArrayList<>();

}
