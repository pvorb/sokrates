package de.vorb.sokrates.parser;

import de.vorb.sokrates.model.PageMetaData;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = NONE)
public class PageMetaDataParserTest {

    private static final ClassPathResource SAMPLE_FILE = new ClassPathResource("/sample-with-front-matter.md");

    @Autowired
    private PageMetaDataParser pageMetaDataParser;

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
