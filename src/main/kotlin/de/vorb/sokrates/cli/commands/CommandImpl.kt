package de.vorb.sokrates.cli.commands

import com.beust.jcommander.JCommander
import com.beust.jcommander.Parameter
import com.beust.jcommander.Parameters

sealed class CommandImpl {

    @Parameters(commandNames = arrayOf(New.name), commandDescription = "Create a new page", separators = "=")
    class New : CommandImpl() {

        @Parameter(names = arrayOf("--title", "-t"), required = true, description = "Title of the page")
        var title: String? = null

        @Parameter(names = arrayOf("--slug"), description = "Slug of the page")
        var slug: String? = null

        @Parameter(names = arrayOf("--draft"))
        var draft = false

        companion object {
            const val name = "new"
        }
    }

    @Parameters(commandNames = arrayOf(Edit.name), commandDescription = "Edit a page", separators = "=")
    class Edit : CommandImpl() {
        companion object {
            const val name = "edit"
        }
    }

    @Parameters(commandNames = arrayOf(Build.name), commandDescription = "Build the website", separators = "=")
    class Build : CommandImpl() {
        companion object {
            const val name = "build"
        }
    }

    @Parameters(commandNames = arrayOf(Serve.name), commandDescription = "Serve the website", separators = "=")
    class Serve : CommandImpl() {
        companion object {
            const val name = "serve"
        }
    }

    companion object {
        fun registerCommands(jCommander: JCommander): Unit {
            jCommander.addCommand(New.name, New())
            jCommander.addCommand(Edit.name, Edit())
            jCommander.addCommand(Build.name, Build())
            jCommander.addCommand(Serve.name, Serve())
        }
    }
}
