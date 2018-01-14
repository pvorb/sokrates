package de.vorb.sokrates.model;

import de.vorb.sokrates.generator.pandoc.PandocSourceFileFormat;

import lombok.Data;

import java.nio.file.Path;

@Data
public class SourceFileMatch {

    private final Path file;
    private final Path baseDirectory;
    private final PandocSourceFileFormat format;

}
