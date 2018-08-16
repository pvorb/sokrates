package de.vorb.sokrates.generator.tpl;

import de.vorb.sokrates.db.jooq.tables.pojos.Page;
import de.vorb.sokrates.model.CopyrightYears;
import de.vorb.sokrates.model.PageMetaData;
import de.vorb.sokrates.properties.IndexProperties;
import de.vorb.sokrates.properties.SokratesProperties;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.Writer;
import java.time.Clock;
import java.time.LocalDate;
import java.time.Year;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class TemplateRenderer {

    private final Clock clock;
    private final SokratesProperties sokratesProperties;
    private final List<TemplateEngine> templateEngines;

    public void renderPage(Writer writer, Page page, PageMetaData pageMetaData, String content) {
        final String templateName = pageMetaData.getTemplate();
        final Map<String, Object> context = getRenderingContext(page, pageMetaData, content);
        final Locale locale = Optional.ofNullable(pageMetaData.getLocale())
                .orElse(sokratesProperties.getSite().getDefaultLocale());
        try {
            getEngineForTemplateName(templateName).renderFile(writer, templateName, context, locale);
        } catch (Exception e) {
            log.error("Rendering page '{}' failed", page.getUrl(), e);
        }
    }

    private Map<String, Object> getRenderingContext(Page page, PageMetaData pageMetaData, String content) {
        final Map<String, Object> context = pageMetaData.toMap();
        context.put("url", page.getUrl());
        context.put("content", content);
        context.put("site", sokratesProperties.getSite());
        context.put("copyrightYears", getCopyrightYearsFromPageMetaData(pageMetaData));
        return context;
    }

    private CopyrightYears getCopyrightYearsFromPageMetaData(PageMetaData pageMetaData) {
        return CopyrightYears.of(pageMetaData.getCreatedAt().getYear(), pageMetaData.getLastModifiedAt().getYear());
    }

    public void renderIndexFile(Writer writer, IndexProperties index, List<Page> pages,
            Map<Object, List<Page>> groupedIndexPages, Map<Long, List<String>> pageTags) {
        final String templateName = index.getTemplate();
        final Map<String, Object> context = new HashMap<>();
        context.put("index", index);
        context.put("pages", pages);
        context.put("groupedPages", groupedIndexPages);
        context.put("pageTags", pageTags);
        context.put("site", sokratesProperties.getSite());
        context.put("copyrightYears", getCopyrightYearsFromPages(pages));
        final Locale locale = Optional.ofNullable(index.getLocale())
                .orElse(sokratesProperties.getSite().getDefaultLocale());
        try {
            getEngineForTemplateName(templateName).renderFile(writer, templateName, context, locale);
        } catch (Exception e) {
            log.error("Rendering index '{}' failed", index.getName(), e);
        }
    }

    public void renderTagFile(Writer writer, String tag, String tagContent, PageMetaData metaData,
            Locale locale, List<Page> pages) {
        final Map<String, Object> context;
        if (metaData != null) {
            context = metaData.toMap();
        } else {
            context = PageMetaData.builder().title(tag).build().toMap();
        }

        if (tagContent != null) {
            context.put("content", tagContent);
        }

        context.put("tag", tag);
        context.put("pages", pages);
        context.put("site", sokratesProperties.getSite());
        context.put("copyrightYears", getCopyrightYearsFromPages(pages));

        final String templateName = sokratesProperties.getGenerator().getTagRule().getTemplate();

        try {
            getEngineForTemplateName(templateName).renderFile(writer, templateName, context, locale);
        } catch (Exception e) {
            log.error("Rendering tag '{}' failed", tag, e);
        }
    }

    private CopyrightYears getCopyrightYearsFromPages(List<Page> pages) {
        final Set<Integer> years = new HashSet<>();
        pages.stream().map(Page::getCreatedAt).mapToInt(LocalDate::getYear).min().ifPresent(years::add);
        pages.stream().map(Page::getLastModifiedAt).mapToInt(LocalDate::getYear).max().ifPresent(years::add);
        return CopyrightYears.fromCollection(years).orElseGet(() -> CopyrightYears.singleton(getCurrentYear()));
    }

    public void renderFile(Writer writer, String templateName, Map<String, Object> context, Locale locale)
            throws Exception {
        getEngineForTemplateName(templateName).renderFile(writer, templateName, context, locale);
    }

    private TemplateEngine getEngineForTemplateName(String templateName) {
        return templateEngines.stream().filter(templateEngine -> templateEngine.matches(templateName)).findFirst()
                .orElseThrow(
                        () -> new RuntimeException("No template engine found for template '" + templateName + "'"));
    }

    public int getCurrentYear() {
        return Year.now(clock).getValue();
    }

}
