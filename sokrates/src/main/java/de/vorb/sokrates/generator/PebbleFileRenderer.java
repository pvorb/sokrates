package de.vorb.sokrates.generator;

import de.vorb.sokrates.model.PageMetaData;

import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.error.PebbleException;
import com.mitchellbosecke.pebble.template.PebbleTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class PebbleFileRenderer {

    private final PebbleEngine pebbleEngine;

    public void renderFile(Writer writer, PageMetaData pageMetaData, String content) {
        final String templateName = pageMetaData.getTemplate();
        try {
            final PebbleTemplate pebbleTemplate = pebbleEngine.getTemplate(templateName);
            final Map<String, Object> context = getRenderingContext(pageMetaData, content);
            pebbleTemplate.evaluate(writer, context);
        } catch (PebbleException e) {
            throw new RuntimeException("Unable to render file", e);
        } catch (IOException e) {
            throw new RuntimeException("Unable to write file", e);
        }
    }

    private Map<String, Object> getRenderingContext(PageMetaData pageMetaData, String content) {
        final Map<String, Object> context = new HashMap<>(pageMetaData.toMap());
        context.put("content", content);
        return context;
    }

}
