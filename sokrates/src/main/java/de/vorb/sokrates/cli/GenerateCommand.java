package de.vorb.sokrates.cli;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParentCommand;

@Slf4j
@Command(name = "generate", description = "(Re-)Generates the entire site")
@Getter
public class GenerateCommand implements InjectableCommand {

    @ParentCommand
    private GlobalParameters globalParameters;

    @Option(names = {"-w", "--watch"}, description = "Automatically re-runs site generation on file system changes")
    private boolean watch;

    @Override
    public String getBeanName() {
        return "generateCommand";
    }

}
