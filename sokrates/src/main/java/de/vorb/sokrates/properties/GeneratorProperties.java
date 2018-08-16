package de.vorb.sokrates.properties;

import lombok.Data;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class GeneratorProperties {

    private Path pandocExecutable = Paths.get("/usr", "bin", "pandoc");
    private Map<String, String> extensionMapping = new HashMap<>();
    private List<SourceFileRuleProperties> generateRules = new ArrayList<>();
    private List<SourceFileRuleProperties> copyRules = new ArrayList<>();
    private TagRule tagRule;

}
