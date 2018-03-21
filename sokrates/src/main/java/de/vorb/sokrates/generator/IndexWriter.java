package de.vorb.sokrates.generator;

import de.vorb.sokrates.db.jooq.tables.pojos.Page;
import de.vorb.sokrates.db.repositories.PageRepository;
import de.vorb.sokrates.properties.IndexProperties;
import de.vorb.sokrates.properties.SokratesProperties;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.OrderField;
import org.jooq.SortOrder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
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
    private final PebbleRenderer pebbleRenderer;

    @Transactional
    public void writeIndexFiles() {

        final List<IndexProperties> indexes = sokratesProperties.getIndexes();

        for (final IndexProperties index : indexes) {

            final List<OrderField<?>> orderFields = new ArrayList<>();

            for (final String orderBy : index.getOrderBy()) {
                final String[] parts = orderBy.split(" ");
                final String fieldName = parts[0].toUpperCase();
                final SortOrder sortOrder = SortOrder.valueOf(parts[1]);
                orderFields.add(PAGE.field(fieldName).sort(sortOrder));
            }
            orderFields.add(PAGE.ID.desc());

            final List<Page> indexPages = pageRepository.fetchWithOrderBy(orderFields, index.getLimit());
            final Map<Object, List<Page>> groupedIndexPages = index.getGrouping().groupPages(indexPages.stream());

            try (final BufferedWriter writer = Files.newBufferedWriter(index.getOutputFile(), UTF_8, CREATE,
                    TRUNCATE_EXISTING)) {
                pebbleRenderer.renderIndexFile(writer, index, indexPages, groupedIndexPages);
                log.info("Rendered index \"{}\" to {}", index.getName(), index.getOutputFile());
            } catch (IOException e) {
                log.error("Help!", e);
            }
        }

    }

}
