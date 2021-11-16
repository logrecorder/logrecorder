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
package info.novatec.testit.logrecorder.assertion.blocks

import info.novatec.testit.logrecorder.api.LogEntry
import info.novatec.testit.logrecorder.assertion.LogRecordAssertion

/**
 * Custom assertion block for the [LogRecordAssertion] DSL.
 *
 * This assertion block requires each specified expected message to be found in order,
 * but allows other messages between those expectations.
 *
 * **Example:**
 * ```
 * assertThat(log) {
 *     containsInOrder {
 *          info("hello world")
 *          // there might be other between these two
 *          debug(startsWith("foo")
 *     }
 * }
 * ```
 *
 * @since 1.3.0
 */
internal class ContainsInOrder : AbstractMessagesAssertionBlock() {

    override fun check(entries: List<LogEntry>, expectations: List<ExpectedLogEntry>): List<MatchingResult> {
        var indexOfLastMatch = -1
        return expectations
            .map { expectation ->
                var result: ContainsInOrderMatchingResult? = null
                entries.forEachIndexed { i, entry ->
                    if (i > indexOfLastMatch && result == null) {
                        if (matches(expectation, entry)) {
                            result = ContainsInOrderMatchingResult(entry, expectation)
                            indexOfLastMatch = i
                        }
                    }
                }
                result ?: ContainsInOrderMatchingResult(null, expectation)
            }
    }

    private fun matches(expected: ExpectedLogEntry, actual: LogEntry): Boolean {
        val levelMatches = expected.logLevelMatcher.matches(actual.level)
        val messageMatches = expected.messageMatchers.all { it.matches(actual.message) }
        return levelMatches && messageMatches
    }

    private class ContainsInOrderMatchingResult(
        private val actual: LogEntry?,
        private val expected: ExpectedLogEntry
    ) : MatchingResult {

        override val matches: Boolean
            get() = actual != null

        override fun describe() = if (actual != null) {
            """[${'\u2713'}] ${actual.level} | "${actual.message}""""
        } else {
            """[${'\u2717'}] did not find entry matching: ${expected.logLevelMatcher} | ${expected.messageMatchers}"""
        }

    }

}
