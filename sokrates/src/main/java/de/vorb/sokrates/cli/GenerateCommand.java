package de.vorb.sokrates.cli;

import de.vorb.sokrates.SokratesApp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.builder.SpringApplicationBuilder;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Slf4j
@Command(name = "generate", description = "(Re-)Generates the entire site")
public class GenerateCommand implements Runnable {

    @Option(names = {"-w", "--watch"}, description = "Automatically re-runs site generation on file system changes")
    private boolean watch;

    @Override
    public void run() {
        new SpringApplicationBuilder(SokratesApp.class)
                .web(false)
                .build()
                .run();
    }

}
