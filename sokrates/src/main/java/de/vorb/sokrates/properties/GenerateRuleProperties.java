package de.vorb.sokrates.properties;

import de.vorb.sokrates.generator.pandoc.PandocSourceFileFormat;

import lombok.Data;

import java.nio.file.Path;

@Data
public class GenerateRuleProperties {

    private String pattern;
    private Path baseDirectory;
    private PandocSourceFileFormat format;

}
