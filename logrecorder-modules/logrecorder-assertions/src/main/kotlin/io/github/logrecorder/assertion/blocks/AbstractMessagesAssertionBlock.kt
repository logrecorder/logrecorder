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
import io.github.logrecorder.api.LogRecord
import io.github.logrecorder.assertion.LogRecordAssertion
import io.github.logrecorder.assertion.matchers.ExceptionMatcher
import io.github.logrecorder.assertion.matchers.LogLevelMatcher
import io.github.logrecorder.assertion.matchers.MessageMatcher
import io.github.logrecorder.assertion.matchers.PropertyMatcher

/**
 * Base class for custom assertion blocks for the [LogRecordAssertion] DSL.
 *
 * @since 1.1.0
 */
abstract class AbstractMessagesAssertionBlock : MessagesAssertionBlock {

    private val expectations: MutableList<ExpectedLogEntry> = mutableListOf()

    override fun addExpectation(
        logLevelMatcher: LogLevelMatcher,
        messageMatchers: List<MessageMatcher>,
        propertyMatchers: List<PropertyMatcher>,
        exceptionMatchers: List<ExceptionMatcher>
    ) {
        expectations.add(ExpectedLogEntry(logLevelMatcher, messageMatchers, propertyMatchers, exceptionMatchers))
    }

    override fun check(logRecord: LogRecord) {
        val entries = logRecord.entries
        val results = check(entries, expectations)

        if (results.any { !it.matches }) {
            val message = StringBuilder()
                .appendLine("Log entries do not match expectation:")
                .appendLine("---")
                .appendMatchingResults(results)
                .appendLine("---")
                .appendLine()
                .appendLine("The actual log entries were:")
                .appendLine("---")
                .appendActualLogEntries(entries)
                .appendLine("---")
                .toString()
            throw AssertionError(message.trim())
        }
    }

    private fun StringBuilder.appendMatchingResults(results: List<MatchingResult>) =
        apply { results.forEach { result -> appendLine(result.describe()) } }

    private fun StringBuilder.appendActualLogEntries(entries: List<LogEntry>) =
        apply {
            entries.forEach { entry ->
                val details = listOfNotNull(
                    entry.level.toString(),
                    entry.message,
                    entry.properties.takeIf { it.isNotEmpty() }?.toString(),
                    entry.throwable?.let { format(it) }
                )
                appendLine(details.joinToString(separator = " | "))
            }
        }

    @Throws(AssertionError::class)
    protected abstract fun check(entries: List<LogEntry>, expectations: List<ExpectedLogEntry>): List<MatchingResult>

}
