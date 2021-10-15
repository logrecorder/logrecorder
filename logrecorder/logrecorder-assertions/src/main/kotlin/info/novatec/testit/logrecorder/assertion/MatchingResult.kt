package info.novatec.testit.logrecorder.assertion

import info.novatec.testit.logrecorder.api.LogEntry

data class MatchingResult(
    val index: Int,
    val actual: LogEntry,
    val expected: ExpectedLogEntry,
    val levelMatches: Boolean,
    val messageMatches: Boolean
) {

    val matches: Boolean
        get() = levelMatches && messageMatches

    fun describe() = "[${matchSymbol()}] #$index: ${levelPart()} | ${messagePart()}"

    private fun matchSymbol() = if (matches) '\u2713' else '\u2717'

    private fun levelPart(): String {
        if (levelMatches) {
            return "${actual.level}"
        }
        return "${expected.logLevelMatcher.level} >> ${actual.level}"
    }

    private fun messagePart(): String {
        if (messageMatches) {
            return """"${actual.message}""""
        }
        return """${expected.messageMatchers} > > actual ["${actual.message}"]"""
    }

}
