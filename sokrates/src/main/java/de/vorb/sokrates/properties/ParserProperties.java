package de.vorb.sokrates.properties;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ParserProperties {

    private List<SourceFileMatcherProperties> sourceFileMatchers = new ArrayList<>();

}
