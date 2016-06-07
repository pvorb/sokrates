package de.vorb.sokrates.cli.commands.newitem

import com.beust.jcommander.Parameter
import com.beust.jcommander.Parameters
import com.github.slugify.Slugify
import com.google.common.collect.ImmutableMap
import de.vorb.sokrates.cli.CommonParams
import de.vorb.sokrates.cli.UsagePrinter
import de.vorb.sokrates.cli.commands.CommandParams
import de.vorb.sokrates.cli.commands.InteractiveCommand
import de.vorb.sokrates.config.ItemTypeProperties
import de.vorb.sokrates.config.SokratesProperties
import freemarker.template.Template
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.PrintWriter
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardOpenOption
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*
import javax.ws.rs.core.UriBuilder
import freemarker.template.Configuration as FreemarkerConfiguration

@Component
class NewItemCommand @Autowired constructor(
        private val usagePrinter: UsagePrinter,
        override val commonParams: CommonParams,
        private val sokratesProperties: SokratesProperties,
        private val fileTemplatesConfig: FreemarkerConfiguration) : InteractiveCommand {

    override val name = "new"

    override val commandParams: Params = Params()

    private var newFilePath: Path? = null

    override fun run() {

        if (commandParams.help) {
            val out = PrintWriter(System.out)
            usagePrinter.printUsage(command = name, console = out)
            out.close()
            return
        }

        prepareParams()

        val creationDate: ZonedDateTime = Instant.now().atZone(ZoneId.of(sokratesProperties.website.timezone))

        determineNewFilePath(creationDate)

        try {
            writeNewFile(creationDate)
        } catch(e: java.nio.file.FileAlreadyExistsException) {
            System.err.println("Cannot create file '${newFilePath}'. A file with that name already exists.")
        } catch(e: IOException) {
            System.err.println("Cannot create file: ${e.message}")
        }

    }

    private fun prepareParams() {
        if (commandParams.title.isEmpty()) {
            var accepted = false
            while (!accepted) {
                accepted = fillParamsInteractively()
            }
            println()
        } else if (commandParams.slug.isNullOrBlank()) {
            commandParams.slug = getSlugFromTitle()
        }
    }

    private fun fillParamsInteractively(): Boolean {
        val consoleIn = BufferedReader(InputStreamReader(System.`in`))

        println()

        fillTitle(consoleIn)

        val itemType: ItemTypeProperties = fillItemType(consoleIn)

        fillSlug(consoleIn, itemType)

        fillPath(consoleIn, itemType)

        fillDraft(consoleIn)

        presentResults()

        return parseBoolean(consoleIn, "Are these correct?")

    }

    private fun fillTitle(consoleIn: BufferedReader) {
        var title = commandParams.title
        while (title.isBlank()) {
            print("Title (required): ")
            title = consoleIn.readLine()?.trim()!!
            if (title.startsWith('"') && title.endsWith('"')) {
                commandParams.title = title.substring(1, title.length - 1)
            } else {
                commandParams.title = title
            }
        }
    }

    private fun fillItemType(consoleIn: BufferedReader): ItemTypeProperties {
        val availableItemTypeNames = sokratesProperties.itemTypes.map { type -> type.name }
        if (availableItemTypeNames.size > 1) {
            commandParams.type = determineOption(consoleIn, "Type:", availableItemTypeNames, 0)
        } else {
            commandParams.type = availableItemTypeNames.first()
        }

        return sokratesProperties.itemTypes.find { type ->
            type.name == commandParams.type
        }!!
    }

    private fun fillSlug(consoleIn: BufferedReader, itemType: ItemTypeProperties) {

        val dependsOnSlug =
                itemType.sourcePatterns.first().contains("{slug}")

        if (dependsOnSlug) {
            val proposedSlug =
                    if (commandParams.slug.isNotBlank())
                        commandParams.slug
                    else
                        getSlugFromTitle()

            print("Slug [${proposedSlug}]: ")
            val newSlug = consoleIn.readLine()!!.trim()
            if (newSlug.isBlank()) {
                commandParams.slug = proposedSlug
            } else {
                commandParams.slug = newSlug
            }
        }
    }

    private fun getSlugFromTitle(): String {
        val transformer = Slugify()
        transformer.lowerCase = true
        return transformer.slugify(commandParams.title)
    }

    private fun fillPath(consoleIn: BufferedReader, itemType: ItemTypeProperties) {

        val dependsOnPath =
                itemType.sourcePatterns.first().contains("{path}")

        if (dependsOnPath) {
            var path: String = ""
            while (path.isBlank()) {
                print("Path: ")
                path = consoleIn.readLine()!!.trim()
                if (path.isBlank()) {
                    println("Path must not be empty!")
                }
            }
            commandParams.path = path
        }
    }

    private fun fillDraft(consoleIn: BufferedReader) {
        commandParams.draft = parseBoolean(consoleIn, "Draft:", defaultValue = commandParams.draft)
    }

    private fun presentResults() {

        println("\nUsing the following parameters:")
        println(commandParams)
        System.out.flush()

    }

    private fun determineNewFilePath(creationDate: ZonedDateTime): Path? {
        val pathProperties = ImmutableMap.of(
                "year", String.format("%04d", creationDate.year),
                "month", String.format("%02d", creationDate.monthValue),
                "day", String.format("%02d", creationDate.dayOfMonth),
                "slug", commandParams.slug)

        val itemType = sokratesProperties.itemTypes.find { type -> type.name == commandParams.type }

        var outputPattern = itemType!!.sourcePatterns.first()

        if (outputPattern.indexOf("{path}") > 0) {
            outputPattern = outputPattern.replace("{path}", commandParams.path)
        }

        newFilePath = Paths.get("").resolve(
                UriBuilder.fromPath(outputPattern)
                        .buildFromMap(pathProperties)
                        .toString())

        return newFilePath

    }

    private fun writeNewFile(creationDate: ZonedDateTime) {

        Files.createDirectories(newFilePath?.parent)

        val template: Template = fileTemplatesConfig.getTemplate("new-item.ftl")

        Files.newBufferedWriter(newFilePath, StandardCharsets.UTF_8,
                StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE).use { newFileOut ->

            val templateModel = HashMap<String, Any>()
            templateModel.put("common", commonParams)
            templateModel.put("new", commandParams)
            templateModel.put("creationDate", creationDate.toString())

            templateModel.put("properties", sokratesProperties)

            template.process(templateModel, newFileOut)

        }
    }

    @Parameters(commandNames = arrayOf("new", "n"), commandDescription = "Create a new item", separators = "=")
    inner class Params : CommandParams {

        @Parameter(names = arrayOf("-h", "--help"), help = true, description = "Show help for this command")
        var help: Boolean = false

        @Parameter(names = arrayOf("-t", "--title"), required = true, description = "Title of the item")
        var title: String = ""

        @Parameter(names = arrayOf("--type"), description = "Type of the item")
        var type: String = ""

        @Parameter(names = arrayOf("--path"), description = "Path of the item")
        var path: String = ""

        @Parameter(names = arrayOf("--slug"), description = "Slug of the item")
        var slug: String = ""

        @Parameter(names = arrayOf("--draft"), description = "Create the item as a draft")
        var draft: Boolean = false

        @Parameter(names = arrayOf("--template"), description = "Template for the item")
        var template: String = "post.ftl"

        override fun toString(): String =
                if (path.isNullOrEmpty()) {
                    "Title: $title\nSlug: $slug\nType: $type\nDraft: $draft\n"
                } else {
                    "Title: $title\nPath: $path\nType: $type\nDraft: $draft\n"
                }
    }
}
