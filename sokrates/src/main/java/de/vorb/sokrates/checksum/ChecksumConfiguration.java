package de.vorb.sokrates.checksum;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChecksumConfiguration {

    @Bean
    public ChecksumCalculator checksumCalculator() {
        return new Sha1ChecksumCalculator();
    }

}
