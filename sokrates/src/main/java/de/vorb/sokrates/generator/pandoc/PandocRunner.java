package de.vorb.sokrates.generator.pandoc;

import de.vorb.sokrates.properties.SokratesProperties;

import com.google.common.io.CharStreams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.file.Path;
import java.util.Locale;

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
        final StringWriter output = new StringWriter();

        try {
            final int exitCode = runPandoc(inputFile, locale, sourceFormat, targetFormat, output);

            if (exitCode == 0) {
                return output.toString();
            } else {
                throw new PandocExecutionException("Pandoc exited with code " + exitCode);
            }
        } catch (InterruptedException e) {
            throw new PandocExecutionException("Pandoc was interrupted", e);
        } catch (IOException e) {
            throw new PandocExecutionException("Pandoc failed", e);
        }
    }

    private int runPandoc(Path inputFile, Locale locale,
            PandocSourceFileFormat sourceFormat, PandocTargetFileFormat targetFormat,
            Writer output) throws IOException, InterruptedException {

        final String pandocExecutable = sokratesProperties.getGenerator().getPandocExecutable()
                .toAbsolutePath().toString();

        final Process pandocProcess = new ProcessBuilder(pandocExecutable,
                "--variable=lang:" + locale.toLanguageTag(),
                "--from=" + sourceFormat.getFormat(),
                "--to=" + targetFormat.getFormat(),
                inputFile.toString()
        ).start();

        CharStreams.copy(new InputStreamReader(pandocProcess.getInputStream()), output);

        return pandocProcess.waitFor();
    }

}
