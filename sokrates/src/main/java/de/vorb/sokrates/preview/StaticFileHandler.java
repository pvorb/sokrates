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

package de.vorb.sokrates.preview;

import de.vorb.sokrates.properties.SokratesProperties;

import com.google.common.collect.ImmutableMap;
import io.undertow.io.IoCallback;
import io.undertow.io.Sender;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import io.undertow.util.StatusCodes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Optional;

import static java.nio.file.StandardOpenOption.READ;

@Slf4j
@Component
@RequiredArgsConstructor
public class StaticFileHandler implements HttpHandler {

    private static final Map<String, String> EXTENSION_TO_CONTENT_TYPE_MAP = ImmutableMap.<String, String>builder()
            // plain text
            .put("txt", "text/plain")
            .put("adoc", "text/plain")
            .put("asciidoc", "text/plain")
            .put("md", "text/plain")
            .put("mkd", "text/plain")
            .put("markdown", "text/plain")

            // html/css/xml
            .put("html", "text/html")
            .put("xhtml", "application/xhtml+xml")
            .put("css", "text/css")
            .put("xml", "application/xml")

            // javascript/json
            .put("js", "text/javascript")
            .put("json", "application/json")

            // images
            .put("jpg", "image/jpeg")
            .put("jpeg", "image/jpeg")
            .put("gif", "image/gif")
            .put("png", "image/png")
            .put("ico", "image/x-icon")

            .build();

    private final SokratesProperties sokratesProperties;

    @Override
    public void handleRequest(HttpServerExchange httpServerExchange) throws Exception {

        log.info("Request: {} {}", httpServerExchange.getRequestMethod(), httpServerExchange.getRequestPath());

        final Path requestedFilePath = resolveRequestedFile(httpServerExchange);

        if (!Files.exists(requestedFilePath)) {
            respondWithErrorStatus(httpServerExchange, StatusCodes.NOT_FOUND);
            return;
        } else if (!Files.isReadable(requestedFilePath)) {
            respondWithErrorStatus(httpServerExchange, StatusCodes.FORBIDDEN);
            return;
        }

        httpServerExchange.setStatusCode(StatusCodes.OK);

        setContentTypeByFileExtension(httpServerExchange, requestedFilePath);

        sendStaticFileResponse(httpServerExchange, requestedFilePath);
    }

    private Path resolveRequestedFile(HttpServerExchange httpServerExchange) {
        final Path outputDirectory = sokratesProperties.getDirectory().getOutput();
        return outputDirectory.resolve(Paths.get("." + httpServerExchange.getRequestPath()));
    }

    private void respondWithErrorStatus(HttpServerExchange httpServerExchange, int statusCode) {
        httpServerExchange.setStatusCode(statusCode);
        httpServerExchange.getResponseSender().close();
    }

    private void setContentTypeByFileExtension(HttpServerExchange httpServerExchange, Path requestedFilePath) {
        final Optional<String> fileExtension = getFileExtensionFromPath(requestedFilePath);
        if (fileExtension.isPresent() && EXTENSION_TO_CONTENT_TYPE_MAP.containsKey(fileExtension.get())) {
            httpServerExchange.getResponseHeaders().put(Headers.CONTENT_TYPE,
                    EXTENSION_TO_CONTENT_TYPE_MAP.get(fileExtension.get()));
        } else {
            httpServerExchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/octet-stream");
        }
    }

    private Optional<String> getFileExtensionFromPath(Path requestedFilePath) {

        final String[] fileNameParts = requestedFilePath.getFileName().toString().split("\\.");

        if (fileNameParts.length > 0) {
            return Optional.of(fileNameParts[fileNameParts.length - 1]);
        } else {
            return Optional.empty();
        }
    }

    private void sendStaticFileResponse(HttpServerExchange httpServerExchange, Path requestedFilePath)
            throws IOException {
        final FileChannel fileChannel = FileChannel.open(requestedFilePath, READ);

        httpServerExchange.getResponseSender().transferFrom(fileChannel, new IoCallback() {
            @Override
            public void onComplete(HttpServerExchange httpServerExchange, Sender sender) {
                httpServerExchange.getResponseSender().close();
                log.debug("Successfully sent response");

                try {
                    fileChannel.close();
                } catch (IOException e) {
                    log.warn("Unable to close input stream after successful response", e);
                }
            }

            @Override
            public void onException(HttpServerExchange httpServerExchange, Sender sender, IOException e) {
                log.warn("Error occurred while sending response", e);
                httpServerExchange.getResponseSender().close();

                try {
                    fileChannel.close();
                } catch (IOException e2) {
                    log.warn("Unable to close input stream after unsuccessful response", e2);
                }
            }
        });
    }
}
