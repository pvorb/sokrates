package de.vorb.sokrates.generator;

import de.vorb.sokrates.cli.GenerateCommand;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SiteGenerator implements CommandLineRunner {

    private final GenerateCommand generateCommand;
    private final PageWriter pageWriter;
    private final FileCopier fileCopier;
    private final IndexWriter indexWriter;
    private final TagWriter tagWriter;

    @Override
    public void run(String... args) {
        log.info("Running {}...", generateCommand);

        pageWriter.writePages();
        fileCopier.copyFiles();
        indexWriter.writeIndexFiles();
        tagWriter.writeTagFiles();

        log.info("Done");
    }

}

