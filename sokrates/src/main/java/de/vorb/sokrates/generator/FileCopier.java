package de.vorb.sokrates.generator;

import de.vorb.sokrates.db.repositories.PageRepository;
import de.vorb.sokrates.model.SourceFileMatch;
import de.vorb.sokrates.properties.SokratesProperties;
import de.vorb.sokrates.properties.SourceFileRuleProperties;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class FileCopier {

    private final SokratesProperties sokratesProperties;
    private final SourceFileFinder sourceFileFinder;
    private final PageRepository pageRepository;

    @Transactional(readOnly = true)
    public void copyFiles() {

        final List<SourceFileRuleProperties> copyRules = sokratesProperties.getGenerator().getCopyRules();
        final List<SourceFileMatch> sourceFileMatches =
                sourceFileFinder.findSourceFileMatches(copyRules).collect(Collectors.toList());

        int numberOfCopiedFiles = 0;
        for (final SourceFileMatch sourceFileMatch : sourceFileMatches) {

            if (!Files.isRegularFile(sourceFileMatch.getFilePath())) {
                continue;
            }

            if (pageRepository.containsPageWithSourceFilePath(sourceFileMatch.getFilePath())) {
                log.debug("Not copying file {} because it is a page", sourceFileMatch.getFilePath());
                continue;
            }

            final Path outputFilePath = determineOutputFilePath(sourceFileMatch);

            try {
                Files.copy(sourceFileMatch.getFilePath(), outputFilePath, StandardCopyOption.REPLACE_EXISTING);
                numberOfCopiedFiles++;
            } catch (IOException e) {
                log.error("Could not copy file {} to {}", sourceFileMatch.getFilePath(), outputFilePath);
            }
        }

        log.info("Copied {} files", numberOfCopiedFiles);
    }

    private Path determineOutputFilePath(SourceFileMatch sourceFileMatch) {
        final Path relativePath = sourceFileMatch.getBaseDirectoryPath().relativize(sourceFileMatch.getFilePath());
        final Path outputFilePath = sokratesProperties.getDirectory().getOutput()
                .resolve(relativePath);

        ensureOutputDirectoryExists(outputFilePath.getParent());

        return outputFilePath;
    }

    private void ensureOutputDirectoryExists(Path outputDirectory) {
        try {
            Files.createDirectories(outputDirectory);
        } catch (IOException e) {
            log.error("Unable to create output directory: {}", outputDirectory);
        }
    }
}
