package de.vorb.sokrates.cli.commands

import java.io.BufferedReader
import java.util.regex.Pattern

interface InteractiveCommand : Command {

    fun parseBoolean(reader: BufferedReader, question: String, defaultValue: Boolean = true): Boolean {
        print(question)
        print(if (defaultValue) " [Yn] " else " [yN] ")
        val line = reader.readLine()?.trim()
        if (line == null || line.isEmpty()) {
            return defaultValue
        } else {
            return Regex("y|yes|true|1", RegexOption.IGNORE_CASE).matches(line)
        }
    }

    fun <T> determineOption(reader: BufferedReader, question: String, options: List<T>,
                            defaultIndex: Int = 0): T {

        println(question)

        options.forEachIndexed { i, option -> println(" [$i] $option") }

        print("Select option: [$defaultIndex] ")
        val line = reader.readLine()?.trim()
        if (line == null || line.isEmpty()) {
            return options.get(defaultIndex)
        } else {
            return options.get(Integer.parseUnsignedInt(line))
        }
    }

}
