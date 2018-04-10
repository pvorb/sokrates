package de.vorb.sokrates.generator.tpl.freemarker;

import de.vorb.sokrates.generator.tpl.TemplateEngine;

import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.Writer;
import java.util.Map;

@Slf4j
@Order(1)
@Component
@RequiredArgsConstructor
public class FreemarkerTemplateEngine implements TemplateEngine {

    private final Configuration freemarkerConfiguration;

    @Override
    public boolean matches(String templateName) {
        return templateName.endsWith(".ftl") || templateName.endsWith(".ftlh");
    }

    @Override
    @SneakyThrows
    public void renderFile(Writer writer, String templateName, Map<String, Object> context) {
        final Template template = freemarkerConfiguration.getTemplate(templateName);
        template.process(context, writer);
    }

}
