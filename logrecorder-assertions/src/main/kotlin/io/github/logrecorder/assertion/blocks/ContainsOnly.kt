/*
 * Copyright 2017-2022 the original author or authors.
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
package io.github.logrecorder.assertion.blocks

import io.github.logrecorder.api.LogEntry
import io.github.logrecorder.assertion.LogRecordAssertion

/**
 * Custom assertion block for the [LogRecordAssertion] DSL.
 *
 * This assertion block requires only the specified expected message to be found.
 * But it does not care about the order the messages are in.
 *
 * **Example:**
 * ```
 * assertThat(log) {
 *     containsOnly {
 *          info("hello world")
 *          debug(startsWith("foo") // might have been logged come before the previous message
 *     }
 * }
 * ```
 *
 * @since 2.1.0
 */
internal class ContainsOnly : AbstractMessagesAssertionBlock() {

    override fun check(entries: List<LogEntry>, expectations: List<ExpectedLogEntry>): List<MatchingResult> {
        if (expectations.size != entries.size) {
            handleSizeMismatch(entries, expectations)
        }

        val remainingEntries = entries.toMutableList()
        return expectations
            .map { expectation ->
                val matchedIndex = remainingEntries.indexOfFirst { expectation matches it }
                if (matchedIndex != -1) {
                    val entry = remainingEntries.removeAt(matchedIndex)
                    ContainsOnlyMatchingResult(entry, expectation)
                } else {
                    ContainsOnlyMatchingResult(null, expectation)
                }
            }
    }

    private fun handleSizeMismatch(actual: List<LogEntry>, expectations: List<ExpectedLogEntry>) {
        val message = StringBuilder()
            .append("Amount of log entries does not match (actual: ${actual.size}; expected: ${expectations.size})\n")
            .append("\n")
            .append("Actual Entries:\n")
            .append(actual.joinToString("\n") { """${it.level} | ${it.message}""" })
            .append("\n\n")
            .append("Expected Entries:\n")
            .append(expectations.joinToString("\n") { "${it.logLevelMatcher} | ${it.messageMatchers}" })
        throw AssertionError(message.toString())
    }

    private class ContainsOnlyMatchingResult(
        private val actual: LogEntry?,
        private val expected: ExpectedLogEntry
    ) : MatchingResult {
        override val matches: Boolean = actual != null
        override fun describe() = if (actual != null) describeMatch(actual) else describeNothingMatches(expected)
    }

}
