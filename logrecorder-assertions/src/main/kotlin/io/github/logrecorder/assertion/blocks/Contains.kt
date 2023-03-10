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

/**
 * [AssertionBlock] for the different assertion DSL styles.
 *
 * This assertion block requires each specified expected message to be found,
 * but allows other messages between those expectations and does not care about
 * the order the messages are in.
 *
 * **Examples:**
 * ```
 * // AssertJ style
 * assertThat(log).contains {
 *     info("hello world")
 *     // there might be other between these two
 *     debug(startsWith("foo")
 *     // the debug message might even come before the info
 * }
 *
 * // Kotest style
 * log shouldContain {
 *     info("hello world")
 *     // there might be other between these two
 *     debug(startsWith("foo")
 *     // the debug message might even come before the info
 * }
 * ```
 *
 * @since 1.5.0
 */
internal class Contains : AbstractMessagesAssertionBlock() {

    override fun check(entries: List<LogEntry>, expectations: List<ExpectedLogEntry>): List<MatchingResult> {
        val remainingEntries = entries.toMutableList()
        return expectations
            .map { expectation ->
                val matchedIndex = remainingEntries.indexOfFirst { expectation matches it }
                if (matchedIndex != -1) {
                    val entry = remainingEntries.removeAt(matchedIndex)
                    ContainsMatchingResult(entry, expectation)
                } else {
                    ContainsMatchingResult(null, expectation)
                }
            }
    }

    private class ContainsMatchingResult(
        private val actual: LogEntry?,
        private val expected: ExpectedLogEntry
    ) : MatchingResult {
        override val matches: Boolean = actual != null
        override fun describe() = if (actual != null) describeMatch(actual) else describeNothingMatches(expected)
    }

}
