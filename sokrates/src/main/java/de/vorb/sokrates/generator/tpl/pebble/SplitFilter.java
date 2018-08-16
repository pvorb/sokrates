package de.vorb.sokrates.generator.tpl.pebble;

import com.google.common.collect.ImmutableList;
import com.mitchellbosecke.pebble.extension.Filter;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SplitFilter implements Filter {

    private static final String SEPARATOR_ARG = "separator";
    private static final String INCLUDE_EMPTY_ARG = "includeEmpty";

    @Override
    public List<String> getArgumentNames() {
        return ImmutableList.of(SEPARATOR_ARG, INCLUDE_EMPTY_ARG);
    }

    @Override
    public Object apply(Object input, Map<String, Object> args) {
        if (input == null) {
            return null;
        }

        final String inputString = input.toString();

        final String separator = args.get(SEPARATOR_ARG).toString();
        final boolean includeEmpty = (Boolean) args.getOrDefault(INCLUDE_EMPTY_ARG, false);

        final String[] parts = inputString.split(separator);

        if (includeEmpty) {
            return Arrays.asList(parts);
        } else {
            return Arrays.stream(inputString.split(separator))
                    .filter(Objects::nonNull)
                    .filter(s -> !s.isEmpty())
                    .toArray(String[]::new);
        }
    }
}
