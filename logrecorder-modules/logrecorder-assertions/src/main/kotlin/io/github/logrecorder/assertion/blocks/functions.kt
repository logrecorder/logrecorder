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

private const val MATCH_SYMBOL = '\u2713'
private const val MISMATCH_SYMBOL = '\u2717'

internal fun describeMatch(actual: LogEntry): String {
    val details = listOf(
        actual.level.toString(),
        actual.message,
        actual.properties.takeIf { it.isNotEmpty() }?.toString(),
        actual.throwable?.let { format(it) }
    )
    return "[$MATCH_SYMBOL] ${join(details)}"
}

internal fun describeNothingMatches(expected: ExpectedLogEntry): String {
    val details = listOf(
        expected.logLevelMatcher.toString(),
        expected.messageMatchers.toString(),
        expected.propertyMatchers.takeIf { it.isNotEmpty() }?.toString(),
        expected.exceptionMatchers.takeIf { it.isNotEmpty() }?.toString()
    )
    return "[$MISMATCH_SYMBOL] did not find entry matching: ${join(details)}"
}

internal fun describeMismatch(actual: LogEntry, expected: ExpectedLogEntry): String {
    val details = listOf(
        levelPart(actual, expected),
        messagePart(actual, expected),
        propertiesPart(actual, expected),
        exceptionPart(actual, expected)
    )
    return "[$MISMATCH_SYMBOL] ${join(details)}"
}

private fun levelPart(actual: LogEntry, expected: ExpectedLogEntry): String {
    if (expected matches actual.level) {
        return "${actual.level}"
    }
    return "${expected.logLevelMatcher} >> ${actual.level}"
}

private fun messagePart(actual: LogEntry, expected: ExpectedLogEntry): String {
    if (expected matches actual.message) {
        return actual.message
    }
    return """${expected.messageMatchers} >> actual ["${actual.message}"]"""
}

private fun propertiesPart(actual: LogEntry, expected: ExpectedLogEntry): String? {
    val matches = expected matches actual.properties
    val hasProperties = actual.properties.isNotEmpty()
    val hasPropertyMatchers = expected.propertyMatchers.isNotEmpty()

    if (matches && hasProperties) {
        return "${actual.properties}"
    } else if (!matches && hasPropertyMatchers) {
        return """${expected.propertyMatchers} >> actual ${actual.properties}"""
    }
    return null
}

private fun exceptionPart(actual: LogEntry, expected: ExpectedLogEntry): String? {
    val matches = expected matches actual.throwable
    val hasException = actual.throwable != null
    val hasExceptionMatchers = expected.exceptionMatchers.isNotEmpty()

    if (matches && hasException) {
        return format(actual.throwable!!)
    } else if (!matches && hasExceptionMatchers) {
        return """${expected.exceptionMatchers} >> ${formatNullable(actual.throwable)}"""
    }
    return null
}

private fun join(data: List<String?>) = data.filterNotNull().joinToString(separator = " | ")

internal fun formatNullable(exception: Throwable?): String {
    if (exception == null) return "no exception"
    return format(exception)
}

internal fun format(exception: Throwable): String {
    val name = exception::class.simpleName ?: "unknown-exception"
    val message = exception.message?.let { "\"$it\"" } ?: ""

    return """$name($message)"""
}
