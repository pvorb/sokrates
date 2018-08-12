package de.vorb.sokrates.properties;

import lombok.Data;

import java.nio.file.Path;
import java.nio.file.Paths;

@Data
public class DirectoryProperties {

    private Path output = Paths.get("target", "sokrates");
    private Path templates = Paths.get("src", "site", "templates");

}
