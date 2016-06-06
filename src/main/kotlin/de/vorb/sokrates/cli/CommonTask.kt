package de.vorb.sokrates.cli

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.io.PrintWriter

@Component
class CommonTask @Autowired constructor(
        private val usagePrinter: UsagePrinter,
        private val commonParams: CommonParams) : Task {

    override fun run() {
        if (commonParams.version) {
            printVersionInfo()
        } else {
            PrintWriter(System.out).use { usagePrinter.printUsage(it) }
        }
    }

    fun printVersionInfo(): Unit {
        println("Version: ${CommonTask::class.java.`package`.implementationVersion ?: "undefined"}")
    }

}
