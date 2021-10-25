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
import info.novatec.testit.logrecorder.api.LogLevel
import info.novatec.testit.logrecorder.api.LogLevel.*
import info.novatec.testit.logrecorder.api.LogRecord
import info.novatec.testit.logrecorder.assertion.LogRecordAssertion.Companion.assertThat
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class ContainsInOrderTests {

    @Test
    fun `reference check - all matched`() {
        val log = logWith(
            entry(level = TRACE, message = "trace message"),
            entry(level = DEBUG, message = "debug message"),
            entry(level = INFO, message = "info message"),
            entry(level = WARN, message = "warn message"),
            entry(level = ERROR, message = "error message")
        )
        assertThat(log) {
            containsInOrder {
                trace("trace message")
                debug("debug message")
                info("info message")
                warn("warn message")
                error("error message")
            }
        }
    }

    @Test
    fun `reference check - some not matched`() {
        val log = logWith(
            entry(level = INFO, message = "message #1"),
            entry(level = INFO, message = "message #99")
        )

        val ex = assertThrows<AssertionError> {
            assertThat(log) {
                containsInOrder {
                    info("message #1")
                    info("message #42")
                    info("message #99")
                }
            }
        }
        assertThat(ex).hasMessage(
            """
            Log entries do not match expectation:
            [✓] INFO | "message #1"
            [✗] did not find entry matching: INFO | [equalTo ["message #42"]]
            [✓] INFO | "message #99"
            """.trimIndent()
        )
    }

    fun logWith(vararg entries: LogEntry): LogRecord = TestLogRecord(listOf(*entries))
    fun entry(logger: String = "logger", level: LogLevel = INFO, message: String = "message") =
        LogEntry(logger, level, message)

}
