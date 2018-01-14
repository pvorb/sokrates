package de.vorb.sokrates.parser;

import de.vorb.sokrates.generator.pandoc.PandocSourceFileFormat;
import de.vorb.sokrates.model.SourceFileMatch;
import de.vorb.sokrates.properties.ParserProperties;
import de.vorb.sokrates.properties.SokratesProperties;
import de.vorb.sokrates.properties.SourceFileMatcherProperties;

import com.google.common.jimfs.Jimfs;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

public class SourceFileFinderTest {

    private final FileSystem fs = Jimfs.newFileSystem();
    private final SokratesProperties sokratesProperties = new SokratesProperties();

    private final SourceFileFinder sourceFileFinder = new SourceFileFinder(sokratesProperties);

    private final Path baseDir = fs.getPath("./src/");
    private final Path sourceFile1 = fs.getPath("./src/1.md");
    private final Path sourceFile2 = fs.getPath("./src/2.md");
    private final Path otherFile = fs.getPath("./src/other.txt");

    @Before
    public void setUp() throws Exception {

        Files.createDirectory(sourceFile1.getParent());
        Files.createFile(sourceFile1);
        Files.createFile(sourceFile2);
        Files.createFile(otherFile);

        sokratesProperties.setParser(new ParserProperties());
        final SourceFileMatcherProperties sourceFileMatcherProperties = new SourceFileMatcherProperties();
        sourceFileMatcherProperties.setPattern("./src/*.md");
        sourceFileMatcherProperties.setBaseDirectory(baseDir);
        sourceFileMatcherProperties.setFormat(PandocSourceFileFormat.MARKDOWN);

        sokratesProperties.getParser().setSourceFileMatchers(Collections.singletonList(sourceFileMatcherProperties));
    }

    @Test
    public void findsMatchingFiles() throws Exception {
        assertThat(sourceFileFinder.findSourceFileMatches())
                .containsExactlyInAnyOrder(
                        new SourceFileMatch(sourceFile1, baseDir, PandocSourceFileFormat.MARKDOWN),
                        new SourceFileMatch(sourceFile2, baseDir, PandocSourceFileFormat.MARKDOWN)
                );
    }
}
