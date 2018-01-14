package de.vorb.sokrates.cli;

import de.vorb.sokrates.generator.SiteGenerator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SiteGenerationRunner extends CommandRunner {

    private final SiteGenerator siteGenerator;

    @Override
    public String getCommand() {
        return "generate";
    }

    @Override
    public void runCommand(String... args) {
        siteGenerator.generateSite();
    }

}
