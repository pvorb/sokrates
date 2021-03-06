package de.vorb.sokrates.properties;

import de.vorb.sokrates.generator.pandoc.PandocSourceFileFormat;

import lombok.Data;

import java.nio.file.Path;
import java.util.Locale;

@Data
public class TagRule {

    private String sourceFilePattern;
    private String outputFilePattern;
    private PandocSourceFileFormat format;
    private String template;
    private Path indexOutputFile;
    private String indexTemplate;
    private Locale locale;

}
