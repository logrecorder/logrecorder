package info.novatec.testit.logrecorder.assertion

import info.novatec.testit.logrecorder.api.LogEntry
import info.novatec.testit.logrecorder.api.LogLevel
import info.novatec.testit.logrecorder.api.LogLevel.*
import info.novatec.testit.logrecorder.api.LogRecord

@DslMarker
annotation class DslContext

@DslContext
class LogRecordAssertion(
    private val logRecord: LogRecord
) {

    private val containsInOrder = ContainsInOrder()

    fun containsInOrder(block: ContainsInOrder.() -> Unit) {
        block(containsInOrder)
    }

    private fun check() {
        containsInOrder.check(logRecord)
    }

    companion object {
        fun assertThat(logRecord: LogRecord, block: LogRecordAssertion.() -> Unit) {
            LogRecordAssertion(logRecord).apply(block).check()
        }
    }
}

@DslContext
class ContainsInOrder {

    private val expectations: MutableList<ExpectedLogEntry> = mutableListOf()

    fun trace(message: String) = trace(equalTo(message))
    fun trace(vararg messageMatchers: MessageMatcher) = addExpectation(equalTo(TRACE), messageMatchers.toList())

    fun debug(message: String) = debug(equalTo(message))
    fun debug(vararg messageMatchers: MessageMatcher) = addExpectation(equalTo(DEBUG), messageMatchers.toList())

    fun info(message: String) = info(equalTo(message))
    fun info(vararg messageMatchers: MessageMatcher) = addExpectation(equalTo(INFO), messageMatchers.toList())

    fun warn(message: String) = warn(equalTo(message))
    fun warn(vararg messageMatchers: MessageMatcher) = addExpectation(equalTo(WARN), messageMatchers.toList())

    fun error(message: String) = error(equalTo(message))
    fun error(vararg messageMatchers: MessageMatcher) = addExpectation(equalTo(ERROR), messageMatchers.toList())

    fun any(message: String) = any(equalTo(message))
    fun any(vararg messageMatchers: MessageMatcher) = addExpectation(anyLogLevel(), messageMatchers.toList())

    fun something() = any(matches(".*"))

    private fun addExpectation(logLevelMatcher: LogLevelMatcher, messageMatchers: List<MessageMatcher>) {
        expectations.add(ExpectedLogEntry(logLevelMatcher, messageMatchers))
    }

    fun equalTo(message: String) = EqualMessageMatcher(message)
    fun matches(regex: String) = RegexMessageMatcher(regex)
    fun contains(vararg parts: String) = ContainsMessageMatcher(listOf(*parts))
    fun containsInOrder(vararg parts: String) =
        ContainsInOrderMessageMatcher(listOf(*parts))

    fun startsWith(prefix: String) = StartsWithMessageMatcher(prefix)
    fun endsWith(suffix: String) = EndsWithMessageMatcher(suffix)

    internal fun check(logRecord: LogRecord) {
        val entries = logRecord.entries
        if (expectations.size != entries.size) {
            handleSizeMismatch(entries, expectations)
        }

        val results = entries.zip(expectations)
            .mapIndexed { index, (actual, expected) -> index to check(expected, actual) }

        if (results.any { !it.second.matches }) {
            val description = results.joinToString(separator = "\n") { it.second.describe(it.first) }
            throw AssertionError("Log entries do not match expectation:\n$description")
        }
    }

    private fun handleSizeMismatch(actual: List<LogEntry>, expectations: List<ExpectedLogEntry>) {
        val message = StringBuilder()
            .append("Amount of log entries does not macth (actual: ${actual.size}; expected: ${expectations.size})\n")
            .append("\n")
            .append("Actual Entries:\n")
            .append(actual.joinToString("\n") { """${it.level} ${it.message}""" })
            .append("\n\n")
            .append("Expected Entries:\n")
            .append(expectations.joinToString("\n") { "${it.logLevelMatcher} ${it.messageMatchers}" })
        throw AssertionError(message.toString())
    }

    private fun check(expected: ExpectedLogEntry, actual: LogEntry) =
        MatchingResult(
            actual = actual,
            expected = expected,
            levelMatches = expected.logLevelMatcher.matches(actual.level),
            messageMatches = expected.messageMatchers.all { it.matches(actual.message) }
        )

    private fun equalTo(logLevel: LogLevel) = LogLevelMatcher(logLevel)
    private fun anyLogLevel() = LogLevelMatcher(null)

}

private class ExpectedLogEntry(
    val logLevelMatcher: LogLevelMatcher,
    val messageMatchers: List<MessageMatcher>
)

private data class MatchingResult(
    val actual: LogEntry,
    val expected: ExpectedLogEntry,
    val levelMatches: Boolean,
    val messageMatches: Boolean
) {

    val matches: Boolean
        get() = levelMatches && messageMatches

    fun describe(index: Int) = "[${matchSymbol()}] #$index: ${levelPart()} | ${messagePart()}"

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
