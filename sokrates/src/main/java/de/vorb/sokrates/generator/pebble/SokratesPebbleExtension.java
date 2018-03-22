package de.vorb.sokrates.generator.pebble;

import com.mitchellbosecke.pebble.extension.AbstractExtension;
import com.mitchellbosecke.pebble.extension.Filter;

import java.util.Collections;
import java.util.Map;

public class SokratesPebbleExtension extends AbstractExtension {

    @Override
    public Map<String, Filter> getFilters() {
        return Collections.singletonMap("plain", new PlainTextFilter());
    }

}
