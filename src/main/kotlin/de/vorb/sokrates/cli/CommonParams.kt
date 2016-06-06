package de.vorb.sokrates.cli

import com.beust.jcommander.Parameter
import org.springframework.stereotype.Component

@Component
class CommonParams {

    @Parameter(names = arrayOf("-h", "--help"), help = true, description = "Show help information")
    var help: Boolean = false

    @Parameter(names = arrayOf("--version"), description = "Show version information")
    var version: Boolean = false

}
