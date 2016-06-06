package de.vorb.sokrates.cli.jcommander

import com.beust.jcommander.JCommander
import com.beust.jcommander.ParameterException
import de.vorb.sokrates.cli.ArgsParser
import de.vorb.sokrates.cli.CommonParams
import de.vorb.sokrates.cli.CommonTask
import de.vorb.sokrates.cli.Task
import de.vorb.sokrates.cli.commands.Command
import de.vorb.sokrates.cli.commands.InteractiveCommand
import de.vorb.sokrates.cli.exceptions.UnknownCommandException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.io.PrintWriter

@Component
class JCommanderArgsParser @Autowired constructor(
        private val jCommander: JCommander,
        private val commonParams: CommonParams,
        private val commonTask: CommonTask,
        private val commands: Set<Command>) : ArgsParser {

    override fun parseArguments(vararg args: String): Task {

        jCommander.addObject(commonParams)

        registerCommands()

        try {
            jCommander.parse(*args)
            return findTask()
        } catch (parameterException: ParameterException) {
            val command = findTask()
            if (command is InteractiveCommand) {
                return command
            } else {
                throw parameterException
            }
        }
    }

    private fun registerCommands(): Unit {
        commands.forEach { command ->
            jCommander.addCommand(command.name, command.commandParams)
        }
    }

    private fun findTask(): Task {
        val commandName = jCommander.parsedCommand

        if (commandName == null) {
            return commonTask
        } else {
            val command = commands.find({
                it.name.equals(commandName)
            })
            if (command == null) {
                throw UnknownCommandException(commandName)
            } else {
                return command
            }
        }
    }

}
