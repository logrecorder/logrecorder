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
package io.github.logrecorder.assertion.blocks

import io.github.logrecorder.api.LogEntry
import io.github.logrecorder.assertion.LogRecordAssertion

/**
 * Custom assertion block for the [LogRecordAssertion] DSL.
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
internal class ContainsExactly : AbstractMessagesAssertionBlock() {

    override fun check(entries: List<LogEntry>, expectations: List<ExpectedLogEntry>): List<MatchingResult> {
        if (expectations.size != entries.size) {
            handleSizeMismatch(entries, expectations)
        }

        return entries.zip(expectations)
            .map { (actual, expected) -> check(expected, actual) }
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

    private fun check(expected: ExpectedLogEntry, actual: LogEntry) =
        ContainsExactlyMatchingResult(
            actual = actual,
            expected = expected,
            levelMatches = expected.logLevelMatcher.matches(actual.level),
            messageMatches = expected.messageMatchers.all { it.matches(actual.message) }
        )

    private class ContainsExactlyMatchingResult(
        private val actual: LogEntry,
        private val expected: ExpectedLogEntry,
        private val levelMatches: Boolean,
        private val messageMatches: Boolean
    ) : MatchingResult {

        override val matches: Boolean
            get() = levelMatches && messageMatches

        override fun describe() = "[${matchSymbol()}] ${levelPart()} | ${messagePart()}"

        private fun matchSymbol() = if (matches) '\u2713' else '\u2717'

        private fun levelPart(): String {
            if (levelMatches) {
                return "${actual.level}"
            }
            return "${expected.logLevelMatcher} >> ${actual.level}"
        }

        private fun messagePart(): String {
            if (messageMatches) {
                return actual.message
            }
            return """${expected.messageMatchers} >> actual ["${actual.message}"]"""
        }
    }

}
