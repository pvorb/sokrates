package de.vorb.sokrates.properties;

import de.vorb.sokrates.db.jooq.tables.pojos.Page;

import lombok.RequiredArgsConstructor;
import org.threeten.extra.YearWeek;

import java.time.Year;
import java.time.YearMonth;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
public enum Grouping {

    NONE(page -> "all"),

    BY_DAY_CREATED(Page::getCreatedAt),
    BY_WEEK_CREATED(page -> YearWeek.from(page.getCreatedAt())),
    BY_MONTH_CREATED(page -> YearMonth.from(page.getCreatedAt())),
    BY_YEAR_CREATED(page -> Year.from(page.getCreatedAt())),

    BY_DAY_LAST_MODIFIED(Page::getLastModifiedAt),
    BY_WEEK_LAST_MODIFIED(page -> YearWeek.from(page.getLastModifiedAt())),
    BY_MONTH_LAST_MODIFIED(page -> YearMonth.from(page.getLastModifiedAt())),
    BY_YEAR_LAST_MODIFIED(page -> Year.from(page.getLastModifiedAt()));

    private final Function<Page, Object> classifier;

    public Map<Object, List<Page>> groupPages(Stream<Page> pages) {
        return pages.collect(Collectors.groupingBy(classifier, LinkedHashMap::new, toList()));
    }

}
