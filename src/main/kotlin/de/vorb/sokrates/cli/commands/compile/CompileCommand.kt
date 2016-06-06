package de.vorb.sokrates.cli.commands.compile

import com.beust.jcommander.Parameter
import com.beust.jcommander.Parameters
import de.vorb.sokrates.cli.CommonParams
import de.vorb.sokrates.cli.UsagePrinter
import de.vorb.sokrates.cli.commands.CommandParams
import de.vorb.sokrates.cli.commands.InteractiveCommand
import de.vorb.sokrates.config.SokratesProperties
import freemarker.template.Configuration
import org.springframework.beans.factory.annotation.Autowired

class CompileCommand @Autowired constructor(
        private val usagePrinter: UsagePrinter,
        override val commonParams: CommonParams,
        private val sokratesProperties: SokratesProperties,
        private val fileTemplateConfig: Configuration) : InteractiveCommand {

    override val name = "new"

    override val commandParams: Params = Params()

    override fun run() {

        // TODO this should be moved to CompileCommand
//        {
//            val regexps: NewCommand.SourceRegexps = getRegexpsFromPatterns(itemType.sourcePatterns)
//
//            val matchedItems: Stream<Path> = Files.walk(Paths.get("")).filter { file ->
//                Files.isRegularFile(file)
//                        && regexps.matches(file.toString().replace('\\', '/'))
//            }
//        }

    }


    @Parameters(commandNames = arrayOf("compile"), commandDescription = "Build static site", separators = "=")
    inner class Params : CommandParams {

        @Parameter(names = arrayOf("-h", "--help"), help = true, description = "Show help for this command")
        var help: Boolean = false

        @Parameter(names = arrayOf("--clean"), description = "Force a clean rebuild of the site")
        var clean: Boolean = false

        @Parameter(names = arrayOf("--draft"), description = "Compile items that are marked as draft")
        var draft: Boolean = false

        override fun toString(): String =
                "Clean: $clean\nDraft: $draft"

    }
}
