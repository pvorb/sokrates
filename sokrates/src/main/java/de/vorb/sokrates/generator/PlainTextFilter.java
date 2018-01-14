package de.vorb.sokrates.generator;

import com.mitchellbosecke.pebble.extension.Filter;
import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;

import java.util.List;
import java.util.Map;

public class PlainTextFilter implements Filter {

    private static final PolicyFactory NO_HTML_POLICY = new HtmlPolicyBuilder().toFactory();

    @Override
    public List<String> getArgumentNames() {
        return null;
    }

    @Override
    public Object apply(Object input, Map<String, Object> args) {
        if (input == null) {
            return null;
        }

        return NO_HTML_POLICY.sanitize((String) input);
    }
}
