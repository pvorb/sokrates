package de.vorb.sokrates.checksum;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static java.nio.file.Files.newByteChannel;

public class Sha1ChecksumCalculator implements ChecksumCalculator {

    private static final MessageDigest SHA_1;

    static {
        try {
            SHA_1 = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            throw new AssertionError("JVM does not support SHA-1");
        }
    }

    @Override
    public byte[] calculateChecksum(Path file) {
        final MessageDigest sha1Hash = createSha1Hash();
        final ByteBuffer buffer = ByteBuffer.allocateDirect(8 * 1024);
        try (final ReadableByteChannel byteChannel = newByteChannel(file, StandardOpenOption.READ)) {
            int bytesRead = 0;
            while (bytesRead != -1) {
                bytesRead = byteChannel.read(buffer);
                if (bytesRead > 0) {
                    sha1Hash.update(buffer);
                    buffer.clear();
                }
            }
            return sha1Hash.digest();
        } catch (IOException e) {
            return null;
        }
    }

    private MessageDigest createSha1Hash() {
        try {
            return MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            throw new AssertionError("JVM does not support SHA-1");
        }
    }
}
