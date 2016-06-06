package de.vorb.sokrates.cli.jcommander

import com.beust.jcommander.JCommander
import de.vorb.sokrates.cli.UsagePrinter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.io.Writer

@Component
class JCommanderUsagePrinter @Autowired constructor(private val jCommander: JCommander) : UsagePrinter {

    override var indentation: Int = 0


    override fun printUsage(out: Writer): Unit {
        printUsageCommon(null, out)
    }

    override fun printUsage(command: String, console: Writer): Unit {
        printUsageCommon(command, console)
    }

    private fun printUsageCommon(command: String?, out: Writer): Unit {
        val usageInfo = StringBuilder()
        when (command) {
            null -> jCommander.usage(usageInfo, indentationString())
            else -> jCommander.usage(command, usageInfo, indentationString())
        }
        out.write(usageInfo.toString())
        printRequiredNote(out)
    }

    private fun indentationString() = " ".repeat(indentation)

    private fun printRequiredNote(out: Writer): Unit {
        out.write(indentationString())
        out.write("Options marked with * are required.\n")
    }

}
