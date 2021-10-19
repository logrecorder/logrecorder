/*
 * Copyright 2017-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package info.novatec.testit.logrecorder.assertion

import info.novatec.testit.logrecorder.api.LogEntry

/**
 * Custom assertion blocks for the [LogRecordAssertion] DSL.
 *
 * This assertion block requires all expected messages to be specified with an expectation in the correct order.
 * As an example, it will fail if 5 messages where expected, but only 4 were actually logged.
 *
 * **Example:**
 * ```
 * assertThat(log) {
 *     containsExactly {
 *          info("hello world")
 *          debug(startsWith("foo")
 *     }
 * }
 * ```
 *
 * @since 1.1.0
 */
@DslContext
class ContainsExactly : AbstractAssertionBlock() {

    /**
     * This matcher can be used to skip log messages, that are not of any interest.
     * It will match any massage with any log level.
     */
    fun anything() = any()

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

/**
 * Define a [ContainsExactly] assertion block.
 */
fun LogRecordAssertion.containsExactly(block: ContainsExactly.() -> Unit) =
    addAssertionBlock(ContainsExactly().apply(block))
