package de.vorb.sokrates.parser;

import de.vorb.sokrates.generator.PageMetaDataParser;
import de.vorb.sokrates.generator.YamlConfiguration;
import de.vorb.sokrates.model.PageMetaData;

import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class PageMetaDataParserTest {

    private static final ClassPathResource SAMPLE_FILE = new ClassPathResource("/sample-with-front-matter.md");

    private PageMetaDataParser pageMetaDataParser;

    @Before
    public void setUp() {
        pageMetaDataParser = new PageMetaDataParser(new YamlConfiguration().yamlObjectMapper());
    }

    @Test
    public void parseFrontMatter() throws Exception {
        try (final Reader reader = createSampleFileReader()) {
            final PageMetaData pageMetaData = pageMetaDataParser.parseMetaDataFrom(reader);

            assertThat(pageMetaData.getTitle())
                    .isEqualTo("Sample File With Front Matter");
            assertThat(pageMetaData.getCreatedAt())
                    .isEqualTo(LocalDate.parse("2018-01-05"));
            assertThat(pageMetaData.getLastModifiedAt())
                    .isEqualTo(LocalDate.parse("2018-01-05"));
            assertThat(pageMetaData.getTemplate())
                    .isEqualTo("sokrates.default.peb");
            assertThat(pageMetaData.getTags())
                    .containsExactlyInAnyOrder("test", "sample");
        }
    }

    private Reader createSampleFileReader() throws IOException {
        return new InputStreamReader(SAMPLE_FILE.getInputStream(), StandardCharsets.UTF_8);
    }
}
