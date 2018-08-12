package de.vorb.sokrates.generator.tpl.pebble;

import de.vorb.sokrates.generator.tpl.TemplateEngine;

import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.error.PebbleException;
import com.mitchellbosecke.pebble.template.PebbleTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
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
    public void renderFile(Writer writer, String templateName, Map<String, Object> context, Locale locale) {
        try {
            final PebbleTemplate pebbleTemplate = pebbleEngine.getTemplate(templateName);
            pebbleTemplate.evaluate(writer, context);
        } catch (PebbleException e) {
            throw new RuntimeException("Unable to render file", e);
        } catch (IOException e) {
            throw new RuntimeException("Unable to write file", e);
        }
    }
}
