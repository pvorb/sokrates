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
import com.google.common.annotations.VisibleForTesting;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.validation.Validator;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.stream.Stream;

@Slf4j
public class PageDescriptorFinder {

    public static final Charset CHARSET = StandardCharsets.UTF_8;

    @VisibleForTesting
    static final int MAXIMUM_PATH_DEPTH = 100;

    private static final BiPredicate<Path, BasicFileAttributes> PAGE_DESCRIPTOR_FILTER =
            (path, fileAttributes) -> fileAttributes.isRegularFile() && isJsonFile(path);

    private final ObjectMapper objectMapper;
    private final Validator validator;

    @Inject
    public PageDescriptorFinder(ObjectMapper objectMapper, Validator validator) {
        this.objectMapper = objectMapper;
        this.validator = validator;
    }

    public Stream<PageDescriptor> findPageDescriptors(Path rootDirectory) throws IOException {

        return Files.find(rootDirectory, MAXIMUM_PATH_DEPTH, PAGE_DESCRIPTOR_FILTER, FileVisitOption.FOLLOW_LINKS)
                .filter(Files::isReadable)
                .map(this::readPageDescriptorFile)
                .filter(Objects::nonNull);
    }

    private static boolean isJsonFile(Path path) {
        return path.getFileName().toString().endsWith(".json");
    }

    private PageDescriptor readPageDescriptorFile(Path file) {

        try (final BufferedReader pageDescriptorReader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {

            final PageDescriptor pageDescriptor = objectMapper.readValue(pageDescriptorReader, PageDescriptor.class);
            pageDescriptor.setDescriptorFile(file);

            validator.validate(pageDescriptor);

            return pageDescriptor;

        } catch (IOException e) {
            log.warn("Unable to read file '{}'", file, e);
            return null;
        }
    }
}
