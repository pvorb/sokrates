package de.vorb.sokrates.parser;

import de.vorb.sokrates.generator.SourceFileFinder;
import de.vorb.sokrates.generator.pandoc.PandocSourceFileFormat;
import de.vorb.sokrates.model.SourceFileMatch;
import de.vorb.sokrates.properties.GeneratorProperties;
import de.vorb.sokrates.properties.SokratesProperties;
import de.vorb.sokrates.properties.SourceFileRuleProperties;

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

    private final SourceFileFinder sourceFileFinder = new SourceFileFinder();

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

        sokratesProperties.setGenerator(new GeneratorProperties());
        final SourceFileRuleProperties sourceFileRuleProperties = new SourceFileRuleProperties();
        sourceFileRuleProperties.setPattern("./src/*.md");
        sourceFileRuleProperties.setBaseDirectory(baseDir);
        sourceFileRuleProperties.setFormat(PandocSourceFileFormat.MARKDOWN);

        sokratesProperties.getGenerator().setGenerateRules(Collections.singletonList(sourceFileRuleProperties));
    }

    @Test
    public void findsMatchingFiles() {
        assertThat(sourceFileFinder.findSourceFileMatches(sokratesProperties.getGenerator().getGenerateRules()))
                .containsExactlyInAnyOrder(
                        new SourceFileMatch(sourceFile1, baseDir, PandocSourceFileFormat.MARKDOWN),
                        new SourceFileMatch(sourceFile2, baseDir, PandocSourceFileFormat.MARKDOWN)
                );
    }
}
