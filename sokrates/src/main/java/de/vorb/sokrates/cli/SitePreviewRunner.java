package de.vorb.sokrates.cli;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SitePreviewRunner extends CommandRunner {

    @Override
    public String getCommand() {
        return "preview";
    }

    @Override
    protected boolean closesAutomatically() {
        return false;
    }

    @Override
    protected void runCommand(String... args) {
        log.info("preview...");
    }

}
