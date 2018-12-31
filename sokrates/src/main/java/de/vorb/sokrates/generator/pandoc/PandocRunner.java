package de.vorb.sokrates.generator.pandoc;

import de.vorb.sokrates.properties.SokratesProperties;

import com.google.common.io.CharStreams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.Locale;

import static java.nio.charset.StandardCharsets.UTF_8;

@Slf4j
@Component
@RequiredArgsConstructor
public class PandocRunner {

    private final SokratesProperties sokratesProperties;

    public String convertFile(
            Path file,
            Locale locale,
            PandocSourceFileFormat sourceFormat,
            PandocTargetFileFormat targetFormat) {

        final Path inputFile = file.toAbsolutePath();

        try {
            return runPandoc(inputFile, locale, sourceFormat, targetFormat);
        } catch (InterruptedException e) {
            throw new PandocExecutionException("Pandoc was interrupted", e);
        } catch (IOException e) {
            throw new PandocExecutionException("Pandoc failed", e);
        }
    }

    private String runPandoc(Path inputFile, Locale locale,
            PandocSourceFileFormat sourceFormat, PandocTargetFileFormat targetFormat)
            throws IOException, InterruptedException {

        final String pandocExecutable = sokratesProperties.getGenerator().getPandocExecutable()
                .toAbsolutePath().toString();

        final Process pandocProcess = new ProcessBuilder(pandocExecutable,
                "--variable=lang:" + locale.toLanguageTag(),
                "--from=" + sourceFormat.getFormat(),
                "--to=" + targetFormat.getFormat(),
                inputFile.toString()
        ).start();

        final int exitCode = pandocProcess.waitFor();
        if (exitCode == 0) {
            return CharStreams.toString(new InputStreamReader(pandocProcess.getInputStream(), UTF_8));
        } else {
            throw new PandocExecutionException(
                    "Pandoc exited with code " + exitCode + " while processing file " + inputFile + "; Error:\n" +
                            CharStreams.toString(new InputStreamReader(pandocProcess.getErrorStream(), UTF_8)));
        }
    }

}
