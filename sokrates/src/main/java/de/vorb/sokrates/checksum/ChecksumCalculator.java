package de.vorb.sokrates.checksum;

import java.nio.file.Path;

public interface ChecksumCalculator {

    byte[] calculateChecksum(Path file);

}
