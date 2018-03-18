package de.vorb.sokrates.properties;

import lombok.Data;

import java.nio.file.Path;

@Data
public class CopyRuleProperties {

    private String pattern;
    private Path baseDirectory;

}
