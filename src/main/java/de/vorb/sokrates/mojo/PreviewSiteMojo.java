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

import de.vorb.sokrates.preview.StaticFileHandler;

import io.undertow.Undertow;
import lombok.extern.slf4j.Slf4j;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

import javax.inject.Inject;

/**
 * Run a preview of this site in an embedded web server.
 */
@Slf4j
@Mojo(name = "preview", defaultPhase = LifecyclePhase.NONE)
public class PreviewSiteMojo extends AbstractMojo {

    private final StaticFileHandler staticFileHandler;

    @Inject
    public PreviewSiteMojo(StaticFileHandler staticFileHandler) {
        this.staticFileHandler = staticFileHandler;
    }

    @Override
    public void execute() throws MojoExecutionException {

        System.setProperty("logger.org.xnio", "WARN");

        final Undertow undertow = Undertow.builder()
                .addHttpListener(8080, "localhost", staticFileHandler)
                .build();

        undertow.start();

        log.info("Listening on http://localhost:8080/");

        try {
            Thread.sleep(Long.MAX_VALUE);
        } catch (InterruptedException e) {
            throw new MojoExecutionException("Stop preview", e);
        }
    }
}
