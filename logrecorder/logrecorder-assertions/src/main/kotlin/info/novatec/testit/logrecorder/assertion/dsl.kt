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

    fun check() {
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
    fun trace(messageMatcher: MessageMatcher) = addExpectation(messageMatcher, equalTo(TRACE))

    fun debug(message: String) = debug(equalTo(message))
    fun debug(messageMatcher: MessageMatcher) = addExpectation(messageMatcher, equalTo(DEBUG))

    fun info(message: String) = info(equalTo(message))
    fun info(messageMatcher: MessageMatcher) = addExpectation(messageMatcher, equalTo(INFO))

    fun warn(message: String) = warn(equalTo(message))
    fun warn(messageMatcher: MessageMatcher) = addExpectation(messageMatcher, equalTo(WARN))

    fun error(message: String) = error(equalTo(message))
    fun error(messageMatcher: MessageMatcher) = addExpectation(messageMatcher, equalTo(ERROR))

    fun any(message: String) = any(equalTo(message))
    fun any(messageMatcher: MessageMatcher) = addExpectation(messageMatcher, anyLogLevel())

    fun something() = any(matches(".*"))

    private fun addExpectation(messageMatcher: MessageMatcher, logLevelMatcher: LogLevelMatcher) {
        expectations.add(ExpectedLogEntry(logLevelMatcher, messageMatcher))
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
            .append(expectations.joinToString("\n") { "${it.logLevelMatcher} ${it.messageMatcher}" })
        throw AssertionError(message.toString())
    }

    private fun check(expected: ExpectedLogEntry, actual: LogEntry) =
        MatchingResult(
            actual = actual,
            expected = expected,
            levelMatches = expected.logLevelMatcher.matches(actual.level),
            messageMatches = expected.messageMatcher.matches(actual.message)
        )

    private fun equalTo(logLevel: LogLevel) = LogLevelMatcher(logLevel)
    private fun anyLogLevel() = LogLevelMatcher(null)

}

private class ExpectedLogEntry(
    val logLevelMatcher: LogLevelMatcher,
    val messageMatcher: MessageMatcher
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
        return """${expected.messageMatcher} > > actual ["${actual.message}"]"""
    }

}
