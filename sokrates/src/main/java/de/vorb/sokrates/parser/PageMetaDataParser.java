package de.vorb.sokrates.parser;

import de.vorb.sokrates.model.PageMetaData;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Reader;

@Slf4j
@Component
@RequiredArgsConstructor
public class PageMetaDataParser {

    private final ObjectMapper yamlObjectMapper;

    public PageMetaData parseMetaDataFrom(Reader reader) throws IOException {
        return yamlObjectMapper.readValue(reader, PageMetaData.class);
    }

}
