package de.vorb.sokrates;

import de.vorb.sokrates.cli.GlobalParameters;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import picocli.CommandLine;

@Slf4j
@SpringBootApplication
@RequiredArgsConstructor
public class SokratesApp {

    public static void main(String[] args) {
        final CommandLine cmd = new CommandLine(new GlobalParameters());
        cmd.parseWithHandler(new CommandLine.RunLast(), System.out, args);
    }

}
