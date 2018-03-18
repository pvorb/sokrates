package de.vorb.sokrates.cli;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParentCommand;

@Slf4j
@Command(name = "generate", description = "(Re-)Generates the entire site")
@Data
public class GenerateCommand implements InjectableCommand {

    @Option(names = {"-w", "--watch"}, description = "Automatically re-runs site generation on file system changes")
    private boolean watch;

    @Option(names = {"-f", "--force"}, description = "Forces sokrates to re-generate files that did not change")
    private boolean force;

    @ParentCommand
    private GlobalParameters globalParameters;

    @Override
    public String getBeanName() {
        return "generateCommand";
    }

}
