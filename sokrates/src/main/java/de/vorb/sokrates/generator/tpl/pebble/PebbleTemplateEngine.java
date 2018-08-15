package de.vorb.sokrates.generator.tpl.pebble;

import de.vorb.sokrates.generator.tpl.TemplateEngine;

import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.template.PebbleTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.Writer;
import java.util.Locale;
import java.util.Map;

@Slf4j
@Order(2)
@Component
@RequiredArgsConstructor
class PebbleTemplateEngine implements TemplateEngine {

    private final PebbleEngine pebbleEngine;

    @Override
    public boolean matches(String templateName) {
        return templateName.endsWith(".peb");
    }

    @Override
    public void renderFile(Writer writer, String templateName, Map<String, Object> context, Locale locale)
            throws Exception {
        final PebbleTemplate pebbleTemplate = pebbleEngine.getTemplate(templateName);
        pebbleTemplate.evaluate(writer, context);
    }
}
