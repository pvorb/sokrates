package de.vorb.sokrates.generator.tpl;

import de.vorb.sokrates.db.jooq.tables.pojos.Page;
import de.vorb.sokrates.model.PageMetaData;
import de.vorb.sokrates.properties.IndexProperties;
import de.vorb.sokrates.properties.SokratesProperties;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class TemplateRenderer {

    private final SokratesProperties sokratesProperties;
    private final List<TemplateEngine> templateEngines;

    public void renderPage(Writer writer, Page page, PageMetaData pageMetaData, String content) {
        final String templateName = pageMetaData.getTemplate();
        final Map<String, Object> context = getRenderingContext(page, pageMetaData, content);
        getEngineForTemplateName(templateName).renderFile(writer, templateName, context);
    }

    public Map<String, Object> getRenderingContext(Page page, PageMetaData pageMetaData, String content) {
        final Map<String, Object> context = pageMetaData.toMap();
        context.put("url", page.getUrl());
        context.put("content", content);
        context.put("site", sokratesProperties.getSite());
        return context;
    }

    public void renderIndexFile(Writer writer, IndexProperties index, List<Page> pages,
            Map<Object, List<Page>> groupedIndexPages) {
        final String templateName = index.getTemplate();
        final Map<String, Object> context = new HashMap<>();
        context.put("index", index);
        context.put("pages", pages);
        context.put("groupedPages", groupedIndexPages);
        context.put("site", sokratesProperties.getSite());
        getEngineForTemplateName(templateName).renderFile(writer, templateName, context);
    }

    public void renderTagFile(Writer writer, String tag, String tagContent, PageMetaData metaData, List<Page> pages) {
        final Map<String, Object> context;
        if (metaData != null) {
            context = metaData.toMap();
        } else {
            context = new HashMap<>();
        }

        if (tagContent != null) {
            context.put("content", tagContent);
        }

        if (!context.containsKey("title")) {
            context.put("title", tag);
        }

        context.put("pages", pages);
        context.put("site", sokratesProperties.getSite());

        final String templateName = sokratesProperties.getGenerator().getTagRule().getTemplate();

        getEngineForTemplateName(templateName).renderFile(writer, templateName, context);
    }

    public void renderFile(Writer writer, String templateName, Map<String, Object> context) {
        getEngineForTemplateName(templateName).renderFile(writer, templateName, context);
    }

    private TemplateEngine getEngineForTemplateName(String templateName) {
        return templateEngines.stream().filter(templateEngine -> templateEngine.matches(templateName)).findFirst()
                .orElseThrow(
                        () -> new RuntimeException("No template engine found for template '" + templateName + "'"));
    }

}
