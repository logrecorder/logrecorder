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

import io.github.logrecorder.api.LogLevel.DEBUG
import io.github.logrecorder.api.LogLevel.ERROR
import io.github.logrecorder.api.LogLevel.INFO
import io.github.logrecorder.api.LogLevel.TRACE
import io.github.logrecorder.api.LogLevel.WARN
import io.github.logrecorder.assertion.LogRecordAssertion.Companion.assertThat
import io.github.logrecorder.assertion.containsExactly
import io.github.logrecorder.assertion.logEntry
import io.github.logrecorder.assertion.logRecord
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class ContainsExactlyTests {

    @Test
    fun `reference check - all matched`() {
        val log = logRecord(
            logEntry(level = TRACE, message = "trace message"),
            logEntry(level = DEBUG, message = "debug message"),
            logEntry(level = INFO, message = "info message"),
            logEntry(level = WARN, message = "warn message"),
            logEntry(level = ERROR, message = "error message")
        )

        assertThat(log) containsExactly {
            trace("trace message")
            debug("debug message")
            info("info message")
            warn("warn message")
            error("error message")
        }
    }

    @Test
    fun `throws assertion error if at least one expectation was not matched - simple message matching`() {
        val log = logRecord(
            logEntry(level = INFO, message = "message #1"),
            logEntry(level = INFO, message = "message #2")
        )

        val ex = assertThrows<AssertionError> {
            assertThat(log) containsExactly {
                info("message #1")
                info("message #3")
            }
        }

        assertThat(ex).hasMessage(
            """
            Log entries do not match expectation:
            ---
            [✓] INFO | message #1
            [✗] INFO | [equal to ["message #3"]] >> actual ["message #2"]
            ---
            
            The actual log entries were:
            ---
            INFO | message #1
            INFO | message #2
            ---
            """.trimIndent()
        )
    }

    @Test
    fun `throws assertion error if at least one expectation was not matched - property matching`() {
        val log = logRecord(
            logEntry(level = INFO, message = "message #1", properties = mapOf("foo" to "bar")),
            logEntry(level = INFO, message = "message #2", properties = mapOf("foo" to "bar"))
        )

        val ex = assertThrows<AssertionError> {
            assertThat(log) containsExactly {
                info(message = "message #1", properties = listOf(containsProperty("foo", "bar")))
                info(message = "message #2", properties = listOf(doesNotContainProperty("foo")))
            }
        }

        assertThat(ex).hasMessage(
            """
            Log entries do not match expectation:
            ---
            [✓] INFO | message #1 | {foo=bar}
            [✗] INFO | message #2 | [does not contain property with key [foo]] >> actual {foo=bar}
            ---
            
            The actual log entries were:
            ---
            INFO | message #1 | {foo=bar}
            INFO | message #2 | {foo=bar}
            ---
            """.trimIndent()
        )
    }

    @Test
    fun `size difference between actual and expected log throws assertion error - too few messages`() {
        val log = logRecord(
            logEntry(message = "message #1"),
            logEntry(message = "message #2")
        )

        val ex = assertThrows<AssertionError> {
            assertThat(log) containsExactly {
                any("message #1"); any("message #2"); any("message #3")
            }
        }

        assertThat(ex).hasMessage(
            """
            Amount of log entries does not match (actual: 2; expected: 3)

            Actual Entries:
            INFO | message #1
            INFO | message #2
            
            Expected Entries:
            ANY | [equal to ["message #1"]]
            ANY | [equal to ["message #2"]]
            ANY | [equal to ["message #3"]]
            """.trimIndent()
        )
    }

    @Test
    fun `size difference between actual and expected log throws assertion error - too many messages`() {
        val log = logRecord(
            logEntry(message = "message #1"),
            logEntry(message = "message #2")
        )

        val ex = assertThrows<AssertionError> {
            assertThat(log) containsExactly {
                any("message #1")
            }
        }

        assertThat(ex).hasMessageStartingWith("Amount of log entries does not match (actual: 2; expected: 1)")
    }

}
