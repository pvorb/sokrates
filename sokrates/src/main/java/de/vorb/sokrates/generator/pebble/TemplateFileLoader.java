package de.vorb.sokrates.generator.pebble;

import com.mitchellbosecke.pebble.error.LoaderException;
import com.mitchellbosecke.pebble.loader.Loader;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@RequiredArgsConstructor
public class TemplateFileLoader implements Loader<String> {

    private final Path templateDirectory;

    private Charset charset = StandardCharsets.UTF_8;

    @Setter
    private String prefix = "";
    @Setter
    private String suffix = "";

    @Override
    public Reader getReader(String cacheKey) throws LoaderException {

        String fileName = cacheKey;
        if (prefix != null) {
            fileName = prefix + fileName;
        }
        if (suffix != null) {
            fileName += suffix;
        }

        final Path templateFile = templateDirectory.resolve(fileName);

        try {
            return Files.newBufferedReader(templateFile, charset);
        } catch (IOException e) {
            throw new LoaderException(e, "Could not load template file at " + templateFile);
        }
    }

    @Override
    public void setCharset(String charset) {
        this.charset = Charset.forName(charset);
    }

    @Override
    public String resolveRelativePath(String relativePath, String anchorPath) {
        return Paths.get(anchorPath).resolveSibling(relativePath).toString();
    }

    @Override
    public String createCacheKey(String templateName) {
        return templateName;
    }
}
