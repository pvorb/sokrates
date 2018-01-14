package de.vorb.sokrates.cli;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(
        name = "sokrates",
        description = "Generate static websites or blogs",
        commandListHeading = "Available commands:%n",
        subcommands = {GenerateCommand.class}
)
public class TopLevelCommand {

    @Option(names = {"-h", "--help"}, usageHelp = true, description = "Show this help message")
    private boolean showHelpInfo;

}
