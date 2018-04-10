package de.vorb.sokrates.generator.tpl;

import java.io.Writer;
import java.util.Map;

public interface TemplateEngine {

    boolean matches(String templateName);

    void renderFile(Writer writer, String templateName, Map<String, Object> context);

}
