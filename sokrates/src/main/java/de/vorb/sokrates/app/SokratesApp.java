package de.vorb.sokrates.app;

import de.vorb.sokrates.cli.GlobalParameters;
import de.vorb.sokrates.generator.GeneratorConfiguration;
import de.vorb.sokrates.properties.PropertiesConfiguration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import picocli.CommandLine;

import java.time.Clock;

@Slf4j
@SpringBootApplication
@Import({PropertiesConfiguration.class, GeneratorConfiguration.class})
@RequiredArgsConstructor
public class SokratesApp {

    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone();
    }

    public static void main(String[] args) {
        final CommandLine cmd = new CommandLine(new GlobalParameters());
        cmd.parseWithHandler(new CommandLine.RunLast(), System.out, args);
    }

}
