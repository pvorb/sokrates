package de.vorb.sokrates.generator;

import de.vorb.sokrates.db.jooq.tables.pojos.Page;
import de.vorb.sokrates.db.repositories.PageRepository;
import de.vorb.sokrates.generator.tpl.TemplateRenderer;
import de.vorb.sokrates.properties.IndexProperties;
import de.vorb.sokrates.properties.SokratesProperties;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.SortField;
import org.jooq.SortOrder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static de.vorb.sokrates.db.jooq.Tables.PAGE;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;

@Slf4j
@Component
@RequiredArgsConstructor
public class IndexWriter {

    private final SokratesProperties sokratesProperties;
    private final PageRepository pageRepository;
    private final TemplateRenderer templateRenderer;

    @Transactional
    public void writeIndexFiles() {

        final List<IndexProperties> indexes = sokratesProperties.getIndexes();

        for (final IndexProperties index : indexes) {

            final List<SortField<?>> sortFields = determineOrder(index);

            final List<Page> indexPages = pageRepository.fetchWithOrderBy(sortFields, index.getLimit());
            final Map<Object, List<Page>> groupedIndexPages = index.getGrouping().groupPages(indexPages.stream());

            writeIndexFile(index, indexPages, groupedIndexPages);
        }
    }

    private void writeIndexFile(IndexProperties index, List<Page> indexPages,
            Map<Object, List<Page>> groupedIndexPages) {

        final Path outputFilePath = sokratesProperties.getDirectory().getOutput()
                .resolve(index.getOutputFile());

        ensureOutputDirectoryExists(outputFilePath.getParent());

        try (final BufferedWriter writer = openBufferedWriter(outputFilePath)) {
            templateRenderer.renderIndexFile(writer, index, indexPages, groupedIndexPages);
            log.info("Rendered index \"{}\" to {}", index.getName(), outputFilePath);
        } catch (IOException e) {
            log.error("Could not write index file", e);
        }
    }

    private void ensureOutputDirectoryExists(Path outputDirectory) {
        try {
            Files.createDirectories(outputDirectory);
        } catch (IOException e) {
            log.error("Unable to create output directory: {}", outputDirectory);
        }
    }

    private static BufferedWriter openBufferedWriter(Path filePath) throws IOException {
        return Files.newBufferedWriter(filePath, UTF_8, CREATE, TRUNCATE_EXISTING);
    }

    private List<SortField<?>> determineOrder(IndexProperties index) {

        final List<SortField<?>> orderFields = new ArrayList<>();

        for (final String orderBy : index.getOrderBy()) {
            orderFields.add(parseOrderBy(orderBy));
        }

        ensureDeterministicOrder(orderFields);

        return orderFields;
    }

    private static SortField<?> parseOrderBy(String orderBy) {
        final String[] parts = orderBy.split(" ");
        final String fieldName = parts[0].toUpperCase();
        final SortOrder sortOrder = SortOrder.valueOf(parts[1]);
        return PAGE.field(fieldName).sort(sortOrder);
    }

    private static void ensureDeterministicOrder(List<SortField<?>> orderFields) {
        if (!orderFields.isEmpty()) {
            orderFields.add(PAGE.ID.sort(orderFields.get(0).getOrder()));
        } else {
            orderFields.add(PAGE.ID.desc());
        }
    }

}
