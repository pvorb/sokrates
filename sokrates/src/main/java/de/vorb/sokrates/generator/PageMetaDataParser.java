package de.vorb.sokrates.generator;

import de.vorb.sokrates.model.PageMetaData;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.charset.StandardCharsets.UTF_8;

@Slf4j
@Component
@RequiredArgsConstructor
public class PageMetaDataParser {

    private final ObjectMapper yamlObjectMapper;

    public PageMetaData parseMetaDataFrom(Reader reader) throws IOException {
        return yamlObjectMapper.readValue(reader, PageMetaData.class);
    }

    @Nullable
    public PageMetaData parseMetaDataFrom(Path sourceFilePath) {
        try (final Reader reader = openReader(sourceFilePath)) {
            return parseMetaDataFrom(reader);
        } catch (IOException e) {
            log.error("Could not parse meta data from file {}", sourceFilePath, e);
            return null;
        }
    }

    private BufferedReader openReader(Path file) throws IOException {
        return Files.newBufferedReader(file, UTF_8);
    }
}
