package de.vorb.sokrates.db.repositories;

import de.vorb.sokrates.db.jooq.tables.pojos.Page;
import de.vorb.sokrates.db.jooq.tables.records.PageRecord;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.SelectSeekStepN;
import org.jooq.SortField;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Path;
import java.util.List;

import static de.vorb.sokrates.db.jooq.Tables.PAGE;

@Repository
@Transactional(propagation = Propagation.MANDATORY)
@RequiredArgsConstructor
public class PageRepository {

    private final DSLContext dslContext;

    public Page fetchOneBySourceFilePath(Path sourceFilePath) {
        return dslContext.selectFrom(PAGE)
                .where(PAGE.SOURCE_FILE_PATH.eq(sourceFilePath))
                .fetchOneInto(Page.class);
    }

    public boolean containsPageWithSourceFilePath(Path sourceFilePath) {
        return dslContext.selectOne()
                .from(PAGE)
                .where(PAGE.SOURCE_FILE_PATH.eq(sourceFilePath))
                .execute() != 0;
    }

    public List<Page> fetchWithOrderBy(List<SortField<?>> sortFields, Integer limit) {
        final SelectSeekStepN<PageRecord> select = dslContext.selectFrom(PAGE).orderBy(sortFields);
        if (limit != null) {
            return select.limit(limit).fetchInto(Page.class);
        } else {
            return select.fetchInto(Page.class);
        }
    }

    public void insert(Page page) {
        dslContext.executeInsert(dslContext.newRecord(PAGE, page));
    }

    public void update(Page page) {
        dslContext.executeUpdate(dslContext.newRecord(PAGE, page));
    }

}
