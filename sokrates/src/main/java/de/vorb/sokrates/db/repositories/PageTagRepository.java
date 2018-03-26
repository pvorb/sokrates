package de.vorb.sokrates.db.repositories;

import de.vorb.sokrates.db.jooq.tables.pojos.Page;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.Record1;
import org.jooq.SelectConditionStep;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static de.vorb.sokrates.db.jooq.Tables.PAGE;
import static de.vorb.sokrates.db.jooq.Tables.PAGE_TAG;
import static de.vorb.sokrates.db.jooq.Tables.TAG;
import static org.jooq.impl.DSL.value;

@Repository
@RequiredArgsConstructor
@Transactional(propagation = Propagation.MANDATORY)
public class PageTagRepository {

    private final DSLContext dslContext;

    public Map<String, List<Page>> findPagesOfAllTags() {
        return dslContext.select(TAG.NAME, PAGE.TITLE, PAGE.URL, PAGE.CREATED_AT, PAGE.LAST_MODIFIED_AT, PAGE.LOCALE)
                .from(PAGE_TAG
                        .join(PAGE).on(PAGE.ID.eq(PAGE_TAG.PAGE_ID))
                        .join(TAG).on(TAG.ID.eq(PAGE_TAG.TAG_ID)))
                .orderBy(TAG.NAME.asc(), PAGE.CREATED_AT.desc(), PAGE.ID)
                .fetchGroups(TAG.NAME, Page.class);
    }

    public void savePageTags(Long pageId, Collection<String> tags) {

        final SelectConditionStep<Record1<Long>> expectedTagIds = dslContext.select(TAG.ID)
                .from(TAG)
                .where(TAG.NAME.in(tags));

        dslContext.deleteFrom(PAGE_TAG)
                .where(PAGE_TAG.PAGE_ID.eq(pageId))
                .and(PAGE_TAG.TAG_ID.notIn(expectedTagIds))
                .execute();

        final SelectConditionStep<Record1<Long>> existingPageTagIds = dslContext.select(PAGE_TAG.TAG_ID)
                .from(PAGE_TAG)
                .where(PAGE_TAG.PAGE_ID.eq(pageId));

        dslContext.insertInto(PAGE_TAG)
                .select(dslContext.select(value(pageId), TAG.ID)
                        .from(TAG)
                        .where(TAG.NAME.in(tags)
                                .and(TAG.ID.notIn(existingPageTagIds))))
                .execute();
    }

}
