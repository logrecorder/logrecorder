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

import io.github.logrecorder.api.LogLevel
import io.github.logrecorder.assertion.LogRecordAssertion.Companion.assertThat
import io.github.logrecorder.assertion.containsOnly
import io.github.logrecorder.assertion.logEntry
import io.github.logrecorder.assertion.logRecord
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class ContainsOnlyTests {

    @Test
    fun `reference check - all matched`() {
        val log = logRecord(
            logEntry(level = LogLevel.TRACE, message = "trace message"),
            logEntry(level = LogLevel.DEBUG, message = "debug message"),
            logEntry(level = LogLevel.INFO, message = "info message"),
            logEntry(level = LogLevel.WARN, message = "warn message"),
            logEntry(level = LogLevel.ERROR, message = "error message")
        )

        assertThat(log) containsOnly {
            // random order but all are contained
            info("info message")
            error("error message")
            debug("debug message")
            warn("warn message")
            trace("trace message")
        }
    }

    @Test
    fun `if multiple equal expectations are specified they all need to be matched by different entries`() {
        val log = logRecord(
            logEntry(message = "message #1"),
            logEntry(message = "message #2")
        )

        assertThat(log) containsOnly {
            any(startsWith("message"))
            any(startsWith("message"))
        }
    }

    @Test
    fun `throws assertion error if at least one expectation was not matched`() {
        val log = logRecord(
            logEntry(level = LogLevel.INFO, message = "message #1"),
            logEntry(level = LogLevel.INFO, message = "message #99")
        )

        val ex = assertThrows<AssertionError> {
            assertThat(log) containsOnly {
                info("message #99")
                info("message #42")
            }
        }

        assertThat(ex).hasMessage(
            """
            Log entries do not match expectation:
            ---
            [✓] INFO | message #99
            [✗] did not find entry matching: INFO | [equal to ["message #42"]]
            ---
            
            The actual log entries were:
            ---
            INFO | message #1
            INFO | message #99
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
            assertThat(log) containsOnly {
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
            assertThat(log) containsOnly {
                any("message #1")
            }
        }

        assertThat(ex).hasMessageStartingWith("Amount of log entries does not match (actual: 2; expected: 1)")
    }

}
