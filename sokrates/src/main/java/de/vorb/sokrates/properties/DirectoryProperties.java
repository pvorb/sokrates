package de.vorb.sokrates.properties;

import lombok.Data;

import java.nio.file.Path;

@Data
public class DirectoryProperties {

    private Path output;
    private Path templates;

}
