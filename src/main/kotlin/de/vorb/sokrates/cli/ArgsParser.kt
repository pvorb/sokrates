package de.vorb.sokrates.cli

interface ArgsParser {

    fun parseArguments(vararg args: String): Task

}
