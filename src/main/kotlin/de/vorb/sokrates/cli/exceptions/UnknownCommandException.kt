package de.vorb.sokrates.cli.exceptions

class UnknownCommandException(commandName: String) :
        RuntimeException(String.format("Unkown command '%s'", commandName))
