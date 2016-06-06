package de.vorb.sokrates.cli.commands.common

data class SourceRegexps(val patternComponentRegex: Regex, val sourceRegexps: List<Regex>) {
    fun match(file: String): List<MatchResult> =
            sourceRegexps.mapNotNull { regex -> regex.matchEntire(file) }
}
