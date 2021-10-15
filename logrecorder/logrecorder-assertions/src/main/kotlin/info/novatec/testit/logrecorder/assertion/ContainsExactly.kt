package info.novatec.testit.logrecorder.assertion

import info.novatec.testit.logrecorder.api.LogEntry

@DslContext
class ContainsExactly : AbstractAssertionBlock() {

    /**
     * This matcher can be used to skip log messages, that are not of any interest.
     * It will match any massage with any log level.
     */
    fun something() = any()

    override fun check(entries: List<LogEntry>, expectations: List<ExpectedLogEntry>): List<MatchingResult> {
        if (expectations.size != entries.size) {
            handleSizeMismatch(entries, expectations)
        }

        return entries.zip(expectations)
            .mapIndexed { index, (actual, expected) -> check(index, expected, actual) }
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

    private fun check(index: Int, expected: ExpectedLogEntry, actual: LogEntry) =
        MatchingResult(
            index = index,
            actual = actual,
            expected = expected,
            levelMatches = expected.logLevelMatcher.matches(actual.level),
            messageMatches = expected.messageMatchers.all { it.matches(actual.message) }
        )

}

fun LogRecordAssertion.containsExactly(block: ContainsExactly.() -> Unit) =
    addAssertionBlock(ContainsExactly().apply(block))
