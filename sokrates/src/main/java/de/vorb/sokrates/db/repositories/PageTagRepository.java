package de.vorb.sokrates.db.repositories;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static de.vorb.sokrates.db.jooq.Tables.PAGE_TAG;
import static de.vorb.sokrates.db.jooq.Tables.TAG;
import static org.jooq.impl.DSL.value;

@Repository
@RequiredArgsConstructor
@Transactional(propagation = Propagation.MANDATORY)
public class PageTagRepository {

    private final DSLContext dslContext;

    public void savePageTags(Long pageId, Set<String> tags) {
        dslContext.deleteFrom(PAGE_TAG)
                .where(PAGE_TAG.PAGE_ID.eq(pageId))
                .and(PAGE_TAG.TAG_ID.notIn(dslContext.select(TAG.ID).from(TAG).where(TAG.NAME.in(tags))));

        dslContext.insertInto(PAGE_TAG)
                .columns(PAGE_TAG.PAGE_ID, PAGE_TAG.TAG_ID)
                .select(dslContext.select(value(pageId), TAG.ID)
                        .from(TAG)
                        .where(TAG.NAME.in(tags)))
                .onDuplicateKeyIgnore();
    }

}
