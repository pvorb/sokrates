/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.vorb.sokrates.generator;

import de.vorb.sokrates.model.PageDescriptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.common.jimfs.Jimfs;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.validation.Validation;
import javax.validation.Validator;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.stream.Collectors;

public class PageDescriptorFinderTest {

    private FileSystem fileSystem;
    private PageDescriptorFinder pageDescriptorFinder;

    @Before
    public void setUp() throws Exception {
        fileSystem = Jimfs.newFileSystem();

        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        pageDescriptorFinder = new PageDescriptorFinder(objectMapper, validator);
    }

    @After
    public void tearDown() throws Exception {
        fileSystem.close();
    }

    @Test
    public void findAllPageDescriptorsRecursively() throws Exception {

        createTextFile("src/site/asciidoc/article-1.json",
                "{\"contentFile\":\"article-1.md\",\"title\":\"Example story\",\"createdAt\":\"2017-01-13T12:01:13Z\","
                        + "\"tags\":[\"example\",\"story\"]}");

        createTextFile("src/site/asciidoc/2017/05/article-2.json", "{\"contentFile\":\"article-2.adoc\","
                + "\"title\":\"Asciidoc article\",\"createdAt\":\"2017-05-25T15:12:00Z\",\"tags\":[\"story\"]}");

        createTextFile("src/site/asciidoc/2017/article-3.csv", "a,b,c");

        final List<PageDescriptor> descriptors =
                pageDescriptorFinder.findPageDescriptors(fileSystem.getPath("src/site/asciidoc"))
                        .collect(Collectors.toList());

        boolean a = false;
    }

    private void createTextFile(String filePath, String content) throws IOException {
        final Path textFile = fileSystem.getPath(filePath);
        Files.createDirectories(textFile.getParent());
        Files.write(textFile, content.getBytes(PageDescriptorFinder.CHARSET), StandardOpenOption.CREATE_NEW);
    }

    @Test
    public void circularSymlinkIsResolvedByMaximumDepth() throws Exception {

    }
}
