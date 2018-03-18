package de.vorb.sokrates.db.repositories;

import de.vorb.sokrates.db.jooq.tables.pojos.Tag;
import de.vorb.sokrates.db.jooq.tables.records.TagRecord;

import lombok.RequiredArgsConstructor;
import org.jooq.Batch;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static de.vorb.sokrates.db.jooq.Tables.TAG;

@Repository
@Transactional(propagation = Propagation.MANDATORY)
@RequiredArgsConstructor
public class TagRepository {

    private final DSLContext dslContext;

    public int saveTags(Collection<String> tags) {
        final Set<String> expectedTags = new HashSet<>(tags);

        final List<String> existingTags = dslContext.selectFrom(TAG).where(TAG.NAME.in(tags)).fetch(TAG.NAME);
        expectedTags.removeAll(existingTags);

        final List<TagRecord> newTags = expectedTags.stream()
                .map(tag -> dslContext.newRecord(TAG, new Tag().setName(tag)))
                .collect(Collectors.toList());

        final Batch batch = dslContext.batchInsert(newTags);
        batch.execute();

        return batch.size();
    }

}
