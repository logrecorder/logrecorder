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

import io.github.logrecorder.api.LogLevel
import io.github.logrecorder.assertion.LogRecordAssertion.Companion.assertThat
import io.github.logrecorder.assertion.contains
import io.github.logrecorder.assertion.logEntry
import io.github.logrecorder.assertion.logRecord
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class ContainsTests {

    @Test
    fun `reference check - all matched`() {
        val log = logRecord(
            logEntry(level = LogLevel.TRACE, message = "trace message"),
            logEntry(level = LogLevel.DEBUG, message = "debug message"),
            logEntry(level = LogLevel.INFO, message = "info message"),
            logEntry(level = LogLevel.WARN, message = "warn message"),
            logEntry(level = LogLevel.ERROR, message = "error message")
        )

        assertThat(log) contains {
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

        assertThat(log) {
            contains {
                any(startsWith("message"))
            }
            contains {
                any(startsWith("message"))
                any(startsWith("message"))
            }
        }

        val ex = assertThrows<AssertionError> {
            assertThat(log) contains {
                any(startsWith("message"))
                any(startsWith("message"))
                any(startsWith("message"))
            }
        }

        assertThat(ex).hasMessage(
            """
            Log entries do not match expectation:
            ---
            [✓] INFO | message #1
            [✓] INFO | message #2
            [✗] did not find entry matching: ANY | [starts with ["message"]]
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
    fun `throws assertion error if at least one expectation was not matched - simple message matching`() {
        val log = logRecord(
            logEntry(level = LogLevel.INFO, message = "message #1"),
            logEntry(level = LogLevel.INFO, message = "message #99")
        )

        val ex = assertThrows<AssertionError> {
            assertThat(log) contains {
                info("message #99")
                info("message #42")
                info("message #1")
            }
        }

        assertThat(ex).hasMessage(
            """
            Log entries do not match expectation:
            ---
            [✓] INFO | message #99
            [✗] did not find entry matching: INFO | [equal to ["message #42"]]
            [✓] INFO | message #1
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
    fun `throws assertion error if at least one expectation was not matched - property matching`() {
        val log = logRecord(
            logEntry(level = LogLevel.INFO, message = "message #1", properties = mapOf("foo" to "bar")),
            logEntry(level = LogLevel.INFO, message = "message #99", properties = mapOf("foo" to "bar"))
        )

        val ex = assertThrows<AssertionError> {
            assertThat(log) contains {
                info(message = "message #1", properties = listOf(containsProperty("foo", "bar")))
                info(message = "message #99", properties = listOf(doesNotContainProperty("foo")))
            }
        }

        assertThat(ex).hasMessage(
            """
            Log entries do not match expectation:
            ---
            [✓] INFO | message #1 | {foo=bar}
            [✗] did not find entry matching: INFO | [equal to ["message #99"]] | [does not contain property with key [foo]]
            ---
            
            The actual log entries were:
            ---
            INFO | message #1 | {foo=bar}
            INFO | message #99 | {foo=bar}
            ---
            """.trimIndent()
        )
    }

}
