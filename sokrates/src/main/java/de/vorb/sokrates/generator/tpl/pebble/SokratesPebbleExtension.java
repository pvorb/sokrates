package de.vorb.sokrates.generator.tpl.pebble;

import com.google.common.collect.ImmutableMap;
import com.mitchellbosecke.pebble.extension.AbstractExtension;
import com.mitchellbosecke.pebble.extension.Filter;

import java.util.Map;

public class SokratesPebbleExtension extends AbstractExtension {

    @Override
    public Map<String, Filter> getFilters() {
        return ImmutableMap.of(
                "noHtml", new NoHtmlFilter(),
                "split", new SplitFilter()
        );
    }

}
