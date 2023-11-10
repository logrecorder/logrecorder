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
import io.github.logrecorder.api.LogLevel.DEBUG
import io.github.logrecorder.api.LogLevel.ERROR
import io.github.logrecorder.api.LogLevel.INFO
import io.github.logrecorder.api.LogLevel.TRACE
import io.github.logrecorder.api.LogLevel.WARN
import io.github.logrecorder.assertion.LogRecordAssertion.Companion.assertThat
import io.github.logrecorder.assertion.containsExactly
import io.github.logrecorder.assertion.logEntry
import io.github.logrecorder.assertion.logRecord
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource

internal class MessagesAssertionBlockDslElementsTests {

    val exampleLogRecord = logRecord(
        logEntry(INFO, "start"),

        logEntry(TRACE, "trace message"),
        logEntry(DEBUG, "debug message"),
        logEntry(INFO, "info message"),
        logEntry(WARN, "warn message"),
        logEntry(ERROR, "error message"),

        logEntry(INFO, "end")
    )

    @Test
    fun `smoke test example`() {
        assertThat(exampleLogRecord) {
            containsExactly {
                info("start")
                trace("trace message")
                debug("debug message")
                info("info message")
                warn("warn message")
                error("error message")
                info("end")
            }
            containsExactly {
                info(startsWith("start"))
                trace(contains("trace"))
                debug(equalTo("debug message"))
                info(matches(".+"))
                warn(containsInOrder("w", "ar", "n"))
                error(contains("error", "message"))
                info(endsWith("end"))
            }
        }
    }

    @Test
    fun `anything matcher matches all log levels and messages`() {
        assertThat(exampleLogRecord) {
            containsExactly {
                info("start")
                anything()
                anything()
                anything()
                anything()
                anything()
                info("end")
            }
        }
    }

    @Nested
    inner class LogLevelMatcherDslElements {

        val logLevelsAndCorrespondingMatchers: List<Pair<LogLevel, MessagesAssertionBlock.() -> Unit>> =
            listOf(
                TRACE to { trace() },
                DEBUG to { debug() },
                INFO to { info() },
                WARN to { warn() },
                ERROR to { error() },
            )

        @TestFactory
        fun `specific level matchers match entries with expected log level`() =
            logLevelsAndCorrespondingMatchers.map { (level, matcher) ->
                dynamicTest("$level") {
                    val log = logRecord(logEntry(level = level))
                    assertThat(log) { containsExactly(matcher) }
                }
            }

        @TestFactory
        fun `specific level matchers do not match entries with different log levels`() =
            logLevelsAndCorrespondingMatchers.flatMap { (level, matcher) ->
                LogLevel.values()
                    .filter { it != level }
                    .map { wrongLevel ->
                        dynamicTest("$level != $wrongLevel") {
                            assertThrows<AssertionError> {
                                val log = logRecord(logEntry(level = wrongLevel))
                                assertThat(log) { containsExactly(matcher) }
                            }
                        }
                    }
            }

        @ParameterizedTest
        @EnumSource(LogLevel::class)
        fun `any matcher matches all log levels`(logLevel: LogLevel) {
            assertThat(logRecord(logEntry(logLevel))) { containsExactly { any() } }
            assertThat(logRecord(logEntry(logLevel, "foo"))) { containsExactly { any("foo") } }
            assertThat(logRecord(logEntry(logLevel, "bar"))) { containsExactly { any(equalTo("bar")) } }
        }

    }

    @Nested
    inner class MessageMatcherDslElements {

        val log = logRecord(logEntry(message = "Foo bar XUR"))

        @Nested
        @DisplayName("equalTo(..)")
        inner class EqualTo {

            @Test
            fun `exactly matching message is OK`() {
                assertThat(log) { containsExactly { any(equalTo("Foo bar XUR")) } }
            }

            @Test
            fun `not matching message throws assertion error`() {
                assertThrows<AssertionError> {
                    assertThat(log) { containsExactly { any(equalTo("something else")) } }
                }
            }

        }

        @Nested
        @DisplayName("matches(..)")
        inner class Matches {

            @Test
            fun `message matching the RegEx is OK`() {
                assertThat(log) { containsExactly { any(matches("Foo.+")) } }
            }

            @Test
            fun `message not matching the RegEx throws assertion error`() {
                assertThrows<AssertionError> {
                    assertThat(log) { containsExactly { any(matches("Bar.+")) } }
                }
            }

        }

        @Nested
        @DisplayName("contains(..)")
        inner class Contains {

            @Test
            fun `message containing all supplied parts in any order is OK`() {
                assertThat(log) { containsExactly { any(contains("XUR", "Foo")) } }
            }

            @Test
            fun `message containing non of the parts throws assertion error`() {
                assertThrows<AssertionError> {
                    assertThat(log) { containsExactly { any(contains("message")) } }
                }
            }

        }

        @Nested
        @DisplayName("containsInOrder(..)")
        inner class ContainsInOrderTests {

            @Test
            fun `message containing all supplied parts in order is OK`() {
                assertThat(log) { containsExactly { any(containsInOrder("Foo", "XUR")) } }
            }

            @Test
            fun `message containing all supplied parts but not in order throws assertion error`() {
                assertThrows<AssertionError> {
                    assertThat(log) { containsExactly { any(containsInOrder("XUR", "Foo")) } }
                }
            }

        }

        @Nested
        @DisplayName("startsWith(..)")
        inner class StartsWith {

            @Test
            fun `message starting with prefix is OK`() {
                assertThat(log) { containsExactly { any(startsWith("Foo ")) } }
            }

            @Test
            fun `message not starting with prefix throws assertion error`() {
                assertThrows<AssertionError> {
                    assertThat(log) { containsExactly { any(startsWith("message")) } }
                }
            }

        }

        @Nested
        @DisplayName("endsWith(..)")
        inner class EndsWith {

            @Test
            fun `message starting with prefix is OK`() {
                assertThat(log) { containsExactly { any(endsWith(" XUR")) } }
            }

            @Test
            fun `message not starting with prefix throws assertion error`() {
                assertThrows<AssertionError> {
                    assertThat(log) { containsExactly { any(endsWith("message")) } }
                }
            }

        }

    }

}
