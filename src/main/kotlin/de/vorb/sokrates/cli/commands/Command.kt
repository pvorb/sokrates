package de.vorb.sokrates.cli.commands

import de.vorb.sokrates.cli.CommonParams
import de.vorb.sokrates.cli.Task

interface Command : Task {

    val name: String
    val commonParams: CommonParams
    val commandParams: CommandParams

}
