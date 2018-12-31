package de.vorb.sokrates.db.repositories;

import de.vorb.sokrates.db.jooq.tables.pojos.Page;
import de.vorb.sokrates.db.jooq.tables.records.PageRecord;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.SelectConditionStep;
import org.jooq.SelectSeekStepN;
import org.jooq.SelectWhereStep;
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

    public List<Page> fetchWithWhereAndOrderBy(String where, List<SortField<?>> sortFields, Integer limit) {
        final SelectWhereStep<PageRecord> select = dslContext.selectFrom(PAGE);
        final SelectConditionStep<PageRecord> selectCondition;
        if (where != null) {
            selectCondition = select.where(where);
        } else {
            selectCondition = select.where("1 = 1");
        }
        final SelectSeekStepN<PageRecord> orderedSelect = selectCondition.orderBy(sortFields);
        if (limit != null) {
            return orderedSelect.limit(limit).fetchInto(Page.class);
        } else {
            return orderedSelect.fetchInto(Page.class);
        }
    }

    public void insert(Page page) {
        final PageRecord storedRecord = dslContext.insertInto(PAGE)
                .set(dslContext.newRecord(PAGE, page))
                .returning(PAGE.ID)
                .fetchOne();
        page.setId(storedRecord.getId());
    }

    public void update(Page page) {
        dslContext.executeUpdate(dslContext.newRecord(PAGE, page));
    }

}
