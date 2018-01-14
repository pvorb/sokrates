package de.vorb.sokrates.cli;

import org.springframework.beans.BeansException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Arrays;

public abstract class CommandRunner implements CommandLineRunner, ApplicationContextAware {

    private ApplicationContext applicationContext;

    public abstract String getCommand();

    protected boolean closesAutomatically() {
        return true;
    }

    @Override
    public final void run(String... args) {

        if (args.length == 0) {
            return;
        }

        final String command = args[0];

        if (command.equals(getCommand())) {
            runCommand(getRemainingArgs(args));

            if (closesAutomatically()) {
                SpringApplication.exit(applicationContext);
            }
        }
    }

    private String[] getRemainingArgs(String[] args) {
        if (args.length == 1) {
            return new String[0];
        } else {
            return Arrays.copyOfRange(args, 1, args.length - 1);
        }
    }

    protected abstract void runCommand(String... args);

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
