package de.vorb.sokrates.generator;

import de.vorb.sokrates.generator.pandoc.PandocSourceFileFormat;
import de.vorb.sokrates.model.SourceFileMatch;
import de.vorb.sokrates.properties.SokratesProperties;
import de.vorb.sokrates.properties.GenerateRuleProperties;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import static java.nio.file.FileVisitOption.FOLLOW_LINKS;

@Slf4j
@Component
@RequiredArgsConstructor
public class SourceFileFinder {

    private static final int MAX_DEPTH = 100;

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    private final SokratesProperties sokratesProperties;

    public Stream<SourceFileMatch> findSourceFileMatches() {
        return sokratesProperties.getGenerator().getGenerateRules().stream()
                .flatMap(this::findSourceFileMatches);
    }

    private Stream<SourceFileMatch> findSourceFileMatches(GenerateRuleProperties sourceFileMatcher) {
        try {
            final String pattern = sourceFileMatcher.getPattern();
            final Path baseDirectory = sourceFileMatcher.getBaseDirectory();
            final PandocSourceFileFormat format = sourceFileMatcher.getFormat();

            final Stream<Path> sourceFileCandidates =
                    Files.walk(baseDirectory, MAX_DEPTH, FOLLOW_LINKS);

            return sourceFileCandidates
                    .filter(candidateFile -> pathMatchesPattern(candidateFile, pattern))
                    .map(sourceFile -> new SourceFileMatch(sourceFile, baseDirectory, format));
        } catch (IOException e) {
            log.error("Exception using {}", sourceFileMatcher, e);
            return Stream.empty();
        }
    }

    private boolean pathMatchesPattern(Path file, String pattern) {
        final String filePath = file.toString().replace('\\', '/');
        return antPathMatcher.match(pattern, filePath);
    }

}
