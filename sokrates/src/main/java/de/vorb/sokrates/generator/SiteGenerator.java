package de.vorb.sokrates.generator;

import de.vorb.sokrates.cli.GenerateCommand;
import de.vorb.sokrates.generator.pandoc.PandocRunner;
import de.vorb.sokrates.model.PageMetaData;
import de.vorb.sokrates.model.SourceFileMatch;
import de.vorb.sokrates.parser.PageMetaDataParser;
import de.vorb.sokrates.parser.SourceFileFinder;
import de.vorb.sokrates.properties.SokratesProperties;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import static com.google.common.io.Files.getFileExtension;
import static com.google.common.io.Files.getNameWithoutExtension;
import static de.vorb.sokrates.generator.pandoc.PandocTargetFileFormat.HTML5;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;

@Slf4j
@Component
@ConditionalOnBean(name = "generateCommand")
@RequiredArgsConstructor
public class SiteGenerator implements ApplicationRunner {

    private final GenerateCommand generateCommand;
    private final DSLContext dslContext;
    private final SokratesProperties sokratesProperties;
    private final SourceFileFinder sourceFileFinder;
    private final PageMetaDataParser pageMetaDataParser;
    private final PandocRunner pandocRunner;
    private final PebbleFileRenderer pebbleFileRenderer;

    @Override
    public void run(ApplicationArguments args) {
        log.info("Generate command: {}", generateCommand);
        generateSite();
    }

    private void generateSite() {
        final Path outputDirectory = sokratesProperties.getDirectory().getOutput();
        ensureOutputDirectoryExists(outputDirectory);

        sourceFileFinder
                .findSourceFileMatches()
                .forEach(this::processSourceFile);
    }

    private void ensureOutputDirectoryExists(Path outputDirectory) {
        try {
            Files.createDirectories(outputDirectory);
        } catch (IOException e) {
            log.error("Unable to create output directory: {}", outputDirectory);
        }
    }

    private void processSourceFile(SourceFileMatch sourceFileMatch) {

        final Path sourceFile = sourceFileMatch.getFile();

        try (final Reader reader = openReader(sourceFile)) {

            final Path relativePath = sourceFileMatch.getBaseDirectory().relativize(sourceFile);
            final Path outputDirectory = sokratesProperties.getDirectory().getOutput()
                    .resolve(relativePath)
                    .getParent();

            ensureOutputDirectoryExists(outputDirectory);

            final PageMetaData pageMetaData = pageMetaDataParser.parseMetaDataFrom(reader);

            final Path sourceFileName = Optional.ofNullable(pageMetaData.getAlias())
                    .map(Paths::get)
                    .orElse(sourceFile.getFileName());
            final Path outputFileName = mapFileExtension(sourceFileName);
            final Path outputFile = outputDirectory.resolve(outputFileName);

            final Locale locale = getDocumentLocale(pageMetaData);
            final String htmlContent = pandocRunner.convertFile(sourceFile, locale, sourceFileMatch.getFormat(), HTML5);

            try (final Writer writer = openWriter(outputFile)) {
                pebbleFileRenderer.renderFile(writer, pageMetaData, htmlContent);
                log.info("Rendered file {}", outputFile);
            } catch (IOException e) {
                log.error("Could not write file {}", outputFile);
            }
        } catch (IOException e) {
            log.error("Could not read file {}", sourceFile);
        }
    }

    private BufferedReader openReader(Path file) throws IOException {
        return Files.newBufferedReader(file, UTF_8);
    }

    private Locale getDocumentLocale(PageMetaData pageMetaData) {
        return Optional.ofNullable(pageMetaData.getLocale())
                .orElse(sokratesProperties.getSite().getDefaultLocale());
    }

    private BufferedWriter openWriter(Path file) throws IOException {
        return Files.newBufferedWriter(file, UTF_8, CREATE, TRUNCATE_EXISTING);
    }

    private Path mapFileExtension(Path fileName) {
        final Map<String, String> extensionMapping = sokratesProperties.getGenerator().getExtensionMapping();
        final String fileNameString = fileName.toString();

        final String fileExtension = getFileExtension(fileNameString);
        if (fileExtension.isEmpty() || !extensionMapping.containsKey(fileExtension)) {
            return fileName;
        } else {
            final String fileNameWithoutExtension = getNameWithoutExtension(fileNameString);
            return Paths.get(fileNameWithoutExtension + '.' + extensionMapping.get(fileExtension));
        }
    }
}

