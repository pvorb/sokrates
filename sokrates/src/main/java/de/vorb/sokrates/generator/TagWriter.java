package de.vorb.sokrates.generator;

import de.vorb.sokrates.db.jooq.tables.pojos.Page;
import de.vorb.sokrates.db.repositories.PageTagRepository;
import de.vorb.sokrates.generator.pandoc.PandocRunner;
import de.vorb.sokrates.generator.tpl.TemplateRenderer;
import de.vorb.sokrates.model.PageMetaData;
import de.vorb.sokrates.properties.SokratesProperties;
import de.vorb.sokrates.properties.TagRule;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static de.vorb.sokrates.generator.pandoc.PandocTargetFileFormat.HTML5;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;

@Slf4j
@Component
@RequiredArgsConstructor
public class TagWriter {

    private final SokratesProperties sokratesProperties;
    private final PageTagRepository pageTagRepository;
    private final PageMetaDataParser pageMetaDataParser;
    private final PandocRunner pandocRunner;
    private final TemplateRenderer templateRenderer;

    @Transactional
    public void writeTagFiles() {

        final TagRule tagRule = sokratesProperties.getGenerator().getTagRule();

        final Map<String, List<Page>> pagesOfAllTags = pageTagRepository.findPagesOfAllTags();

        int numberOfWrittenTagFiles = 0;
        for (final Map.Entry<String, List<Page>> tagPagePair : pagesOfAllTags.entrySet()) {

            final String tag = tagPagePair.getKey();
            final List<Page> pages = tagPagePair.getValue();

            final Path sourceFilePath = Paths.get(String.format(tagRule.getSourceFilePattern(), tag));
            final Path outputFilePath = determineOutputFilePath(tag);

            final PageMetaData metaData;
            final String tagContent;
            if (Files.exists(sourceFilePath)) {
                metaData = pageMetaDataParser.parseMetaDataFrom(sourceFilePath);
                tagContent = parseTagContent(sourceFilePath, tagRule, metaData);
            } else {
                metaData = null;
                tagContent = null;
            }

            ensureOutputDirectoryExists(outputFilePath.getParent());

            try (final Writer writer = openWriter(outputFilePath)) {
                templateRenderer.renderTagFile(writer, tag, tagContent, metaData, pages);
                log.info("Rendered tag \"{}\" to {}", tag, outputFilePath);
                numberOfWrittenTagFiles++;
            } catch (IOException e) {
                log.error("Could not write file {}", outputFilePath);
            }
        }

        log.info("Wrote {} tag files", numberOfWrittenTagFiles);

        writeTagIndexFile(pagesOfAllTags.keySet());
    }

    private void writeTagIndexFile(Set<String> tags) {
        final TagRule tagRule = sokratesProperties.getGenerator().getTagRule();
        final Path tagIndexFilePath = determineOutputFilePath("index");
        try (final Writer writer = openWriter(tagIndexFilePath)) {
            templateRenderer.renderFile(writer, tagRule.getIndexTemplate(), Collections.singletonMap("tags", tags));
            log.info("Rendered tag index to {}", tagIndexFilePath);
        } catch (IOException e) {
            log.error("Could not write file {}", tagIndexFilePath);
        }
    }

    private Path determineOutputFilePath(String tag) {
        final TagRule tagRule = sokratesProperties.getGenerator().getTagRule();
        return sokratesProperties.getDirectory().getOutput().resolve(
                Paths.get(String.format(tagRule.getOutputFilePattern(), tag)));
    }

    private String parseTagContent(Path sourceFilePath, TagRule tagRule, PageMetaData metaData) {

        final Locale tagLocale = Optional.ofNullable(metaData)
                .flatMap(meta -> Optional.ofNullable(meta.getLocale()))
                .orElse(sokratesProperties.getSite().getDefaultLocale());

        return pandocRunner.convertFile(sourceFilePath, tagLocale, tagRule.getFormat(), HTML5);
    }

    private void ensureOutputDirectoryExists(Path outputDirectory) {
        try {
            Files.createDirectories(outputDirectory);
        } catch (IOException e) {
            log.error("Unable to create output directory: {}", outputDirectory);
        }
    }

    private BufferedWriter openWriter(Path file) throws IOException {
        return Files.newBufferedWriter(file, UTF_8, CREATE, TRUNCATE_EXISTING);
    }


}
