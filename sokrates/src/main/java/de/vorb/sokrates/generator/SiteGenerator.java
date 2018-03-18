package de.vorb.sokrates.generator;

import de.vorb.sokrates.checksum.ChecksumCalculator;
import de.vorb.sokrates.cli.GenerateCommand;
import de.vorb.sokrates.db.jooq.tables.daos.PageDao;
import de.vorb.sokrates.db.jooq.tables.daos.PageTagDao;
import de.vorb.sokrates.db.jooq.tables.daos.TagDao;
import de.vorb.sokrates.db.jooq.tables.pojos.Page;
import de.vorb.sokrates.db.jooq.tables.pojos.PageTag;
import de.vorb.sokrates.db.jooq.tables.pojos.Tag;
import de.vorb.sokrates.generator.pandoc.PandocRunner;
import de.vorb.sokrates.generator.pandoc.PandocSourceFileFormat;
import de.vorb.sokrates.model.PageMetaData;
import de.vorb.sokrates.model.SourceFileMatch;
import de.vorb.sokrates.parser.PageMetaDataParser;
import de.vorb.sokrates.parser.SourceFileFinder;
import de.vorb.sokrates.properties.SokratesProperties;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
    private final PageDao pageDao;
    private final PageTagDao pageTagDao;
    private final TagDao tagDao;
    private final SokratesProperties sokratesProperties;
    private final SourceFileFinder sourceFileFinder;
    private final PageMetaDataParser pageMetaDataParser;
    private final ChecksumCalculator checksumCalculator;
    private final PandocRunner pandocRunner;
    private final PebbleFileRenderer pebbleFileRenderer;

    @Override
    public void run(ApplicationArguments args) {
        log.info("Running {}...", generateCommand);
        log.info("Wrote {} files", generateIndividualFiles());
        log.info("Done");
    }

    private int generateIndividualFiles() {
        final List<SourceFileMatch> sourceFileMatches =
                sourceFileFinder.findSourceFileMatches().collect(Collectors.toList());

        int numberOfWrittenFiles = 0;
        for (final SourceFileMatch sourceFileMatch : sourceFileMatches) {

            final Path sourceFilePath = sourceFileMatch.getFilePath();
            final byte[] checksum = checksumCalculator.calculateChecksum(sourceFilePath);
            final Page existingPage = pageDao.fetchOneBySourceFilePath(sourceFilePath);

            if (!generateCommand.isForce() && isPageNewOrChanged(checksum, existingPage)) {
                log.debug("Skipping file {} because it did not change");
                continue;
            }

            final PageMetaData pageMetaData = parsePageMetaData(sourceFileMatch);
            if (pageMetaData == null) {
                continue;
            }

            createMissingTags(pageMetaData.getTags());

            final Path outputFilePath = determineOutputFilePath(sourceFileMatch, pageMetaData);

            final Page page = createPage(sourceFileMatch, pageMetaData, outputFilePath, checksum);

            renderPage(page, pageMetaData);

            storePage(page, existingPage);

            linkPageToTags(page, pageMetaData.getTags());

            numberOfWrittenFiles++;
        }

        return numberOfWrittenFiles;
    }

    private boolean isPageNewOrChanged(byte[] newChecksum, Page existingPage) {
        return existingPage != null && Arrays.equals(newChecksum, existingPage.getChecksum());
    }

    private void createMissingTags(Set<String> tags) {
        final String[] tagNames = tags.toArray(new String[0]);
        final Set<String> existingTagNames = tagDao.fetchByName(tagNames).stream()
                .map(Tag::getName)
                .collect(Collectors.toSet());

        final List<Tag> missingTags = tags.stream()
                .filter(tagName -> !existingTagNames.contains(tagName))
                .map(tag -> new Tag().setName(tag))
                .collect(Collectors.toList());

        tagDao.insert(missingTags);

        log.info("Created {} missing tags", missingTags.size());
    }

    private Path determineOutputFilePath(SourceFileMatch sourceFileMatch, PageMetaData pageMetaData) {
        final Path sourceFileName = Optional.ofNullable(pageMetaData.getAlias())
                .map(Paths::get)
                .orElse(sourceFileMatch.getFilePath().getFileName());

        final Path relativePath = sourceFileMatch.getBaseDirectoryPath().relativize(sourceFileMatch.getFilePath());
        final Path outputDirectory = sokratesProperties.getDirectory().getOutput()
                .resolve(relativePath)
                .getParent();
        ensureOutputDirectoryExists(outputDirectory);

        final Path outputFileName = mapFileExtension(sourceFileName);
        return outputDirectory.resolve(outputFileName);
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

    @Nullable
    private PageMetaData parsePageMetaData(SourceFileMatch sourceFileMatch) {

        final Path sourceFilePath = sourceFileMatch.getFilePath();

        try (final Reader reader = openReader(sourceFilePath)) {

            final PageMetaData pageMetaData = pageMetaDataParser.parseMetaDataFrom(reader);

            if (pageMetaData.getLocale() == null) {
                pageMetaData.setLocale(sokratesProperties.getSite().getDefaultLocale());
            }

            return pageMetaData;

        } catch (IOException e) {
            log.error("Could not read file {}", sourceFilePath);
            return null;
        }
    }

    private BufferedReader openReader(Path file) throws IOException {
        return Files.newBufferedReader(file, UTF_8);
    }

    private void ensureOutputDirectoryExists(Path outputDirectory) {
        try {
            Files.createDirectories(outputDirectory);
        } catch (IOException e) {
            log.error("Unable to create output directory: {}", outputDirectory);
        }
    }

    private Page createPage(SourceFileMatch sourceFileMatch, PageMetaData pageMetaData, Path outputFilePath,
            byte[] checksum) {
        return new Page()
                .setSourceFilePath(sourceFileMatch.getFilePath())
                .setSourceFileFormat(sourceFileMatch.getFormat().getFormat())
                .setOutputFilePath(outputFilePath)
                .setTitle(pageMetaData.getTitle())
                .setCreatedAt(pageMetaData.getCreatedAt())
                .setLastModifiedAt(pageMetaData.getLastModifiedAt())
                .setLocale(pageMetaData.getLocale())
                .setChecksum(checksum);
    }

    private void renderPage(Page page, PageMetaData pageMetaData) {

        final Locale locale = getDocumentLocale(page);
        final String htmlContent =
                pandocRunner.convertFile(page.getSourceFilePath(), locale,
                        PandocSourceFileFormat.forString(page.getSourceFileFormat()), HTML5);

        final Path outputFilePath = page.getOutputFilePath();
        try (final Writer writer = openWriter(outputFilePath)) {
            pebbleFileRenderer.renderFile(writer, pageMetaData, htmlContent);
            log.info("Rendered file {} to {}", page.getSourceFilePath(), outputFilePath);
        } catch (IOException e) {
            log.error("Could not write file {}", outputFilePath);
        }

    }

    private Locale getDocumentLocale(Page page) {
        return Optional.ofNullable(page.getLocale())
                .orElse(sokratesProperties.getSite().getDefaultLocale());
    }

    private BufferedWriter openWriter(Path file) throws IOException {
        return Files.newBufferedWriter(file, UTF_8, CREATE, TRUNCATE_EXISTING);
    }

    private void storePage(Page page, Page existingPage) {
        if (existingPage == null) {
            pageDao.insert(page);
        } else {
            page.setId(existingPage.getId());
            pageDao.update(page);
        }
    }

    private void linkPageToTags(Page page, Set<String> tagNames) {

        final Long pageId = pageDao.fetchOneByOutputFilePath(page.getOutputFilePath()).getId();
        final List<Tag> tags = tagDao.fetchByName(tagNames.toArray(new String[0]));

        final List<PageTag> pageTags = tags.stream()
                .map(tag -> new PageTag(pageId, tag.getId()))
                .collect(Collectors.toList());

        pageTagDao.insert(pageTags);
    }
}

