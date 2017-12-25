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

package de.vorb.sokrates.mojo;

import de.vorb.sokrates.DataSourcePoolConfig;
import de.vorb.sokrates.generator.MarkdownFileParser;

import lombok.extern.slf4j.Slf4j;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import javax.inject.Inject;
import java.io.File;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;

import static de.vorb.sokrates.db.jooq.Tables.PAGE;

/**
 * (Re-)Generate the entire site.
 */
@Slf4j
@Mojo(name = "generate", defaultPhase = LifecyclePhase.SITE)
public class GenerateSiteMojo extends AbstractMojo {

    private final MarkdownFileParser markdownFileParser;
    private final DataSourcePoolConfig dataSourcePoolConfig;

    @Inject
    public GenerateSiteMojo(
            MarkdownFileParser markdownFileParser,
            DataSourcePoolConfig dataSourcePoolConfig) {
        this.markdownFileParser = markdownFileParser;
        this.dataSourcePoolConfig = dataSourcePoolConfig;
    }


    @Parameter(defaultValue = "${project.build.directory}/sokrates-site", property = "sokrates.outputDirectory")
    private File outputDirectory;

    @Override
    public void execute() {
        markdownFileParser.doSomething();

        try {
            final int insertedRows = DSL.using(dataSourcePoolConfig.getConnection(), SQLDialect.H2)
                    .insertInto(PAGE)
                    .set(PAGE.PATH, "/test")
                    .set(PAGE.TITLE, "Test")
                    .set(PAGE.CREATED_AT, Timestamp.from(Instant.now()))
                    .set(PAGE.LAST_MODIFIED_AT, Timestamp.from(Instant.now()))
                    .set(PAGE.CHECKSUM, new byte[20])
                    .execute();

            log.info("Inserted {} rows", insertedRows);

        } catch (SQLException e) {
            log.error("Exception", e);
        } finally {
            dataSourcePoolConfig.close();
        }
    }
}
