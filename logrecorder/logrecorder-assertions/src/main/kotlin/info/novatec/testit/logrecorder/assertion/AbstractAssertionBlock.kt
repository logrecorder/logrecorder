package info.novatec.testit.logrecorder.assertion

import info.novatec.testit.logrecorder.api.LogEntry
import info.novatec.testit.logrecorder.api.LogRecord

abstract class AbstractAssertionBlock : AssertionBlock {

    private val expectations: MutableList<ExpectedLogEntry> = mutableListOf()

    override fun addExpectation(logLevelMatcher: LogLevelMatcher, messageMatchers: List<MessageMatcher>) {
        expectations.add(ExpectedLogEntry(logLevelMatcher, messageMatchers))
    }

    override fun check(logRecord: LogRecord) {
        val entries = logRecord.entries
        val results = check(entries, expectations)

        if (results.any { !it.matches }) {
            val description = results.joinToString(separator = "\n", transform = MatchingResult::describe)
            throw AssertionError("Log entries do not match expectation:\n$description")
        }
    }

    @Throws(AssertionError::class)
    abstract fun check(entries: List<LogEntry>, expectations: List<ExpectedLogEntry>): List<MatchingResult>

}
