package de.vorb.sokrates

import com.beust.jcommander.ParameterException
import de.vorb.sokrates.cli.ArgsParser
import de.vorb.sokrates.cli.Task
import de.vorb.sokrates.cli.exceptions.UnknownCommandException
import de.vorb.sokrates.config.SokratesProperties
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.ComponentScan

@ComponentScan
@EnableConfigurationProperties(SokratesProperties::class)
class SokratesApp @Autowired constructor(private val argsParser: ArgsParser) : CommandLineRunner {

    companion object {
        @JvmStatic
        fun main(vararg args: String) {
            try {
                SpringApplication.run(SokratesApp::class.java, *args)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun run(vararg args: String): Unit = try {
        val task: Task = argsParser.parseArguments(*args)
        task.run()
    } catch(unknownCommandException: UnknownCommandException) {
        System.err.println(unknownCommandException.message)
    } catch (parameterException: ParameterException) {
        System.err.println(parameterException.message)
    }
}
