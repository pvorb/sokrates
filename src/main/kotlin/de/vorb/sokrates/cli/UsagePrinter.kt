package de.vorb.sokrates.cli

import java.io.Writer

interface UsagePrinter {

    var indentation: Int

    fun printUsage(out: Writer): Unit

    fun printUsage(command: String, console: Writer): Unit

}
