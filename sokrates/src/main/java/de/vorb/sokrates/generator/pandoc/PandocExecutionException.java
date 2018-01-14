package de.vorb.sokrates.generator.pandoc;

public class PandocExecutionException extends RuntimeException {

    public PandocExecutionException(String message) {
        super(message);
    }

    public PandocExecutionException(String message, Throwable cause) {
        super(message, cause);
    }

}
