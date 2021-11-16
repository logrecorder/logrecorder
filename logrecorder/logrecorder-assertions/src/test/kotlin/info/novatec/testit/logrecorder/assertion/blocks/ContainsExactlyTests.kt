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

import info.novatec.testit.logrecorder.api.LogLevel
import info.novatec.testit.logrecorder.api.LogLevel.*
import info.novatec.testit.logrecorder.assertion.LogRecordAssertion.Companion.assertThat
import info.novatec.testit.logrecorder.assertion.containsExactly
import info.novatec.testit.logrecorder.assertion.logEntry
import info.novatec.testit.logrecorder.assertion.logRecord
import info.novatec.testit.logrecorder.assertion.matchers.MessageMatcher
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.*
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource

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

        assertThat(log) {
            containsExactly {
                trace("trace message")
                debug("debug message")
                info("info message")
                warn("warn message")
                error("error message")
            }
        }
    }

    @Test
    fun `throws assertion error if at least one expectation was not matched`() {
        val log = logRecord(
            logEntry(level = INFO, message = "message #1"),
            logEntry(level = INFO, message = "message #2")
        )

        val ex = assertThrows<AssertionError> {
            assertThat(log) {
                containsExactly {
                    info("message #1")
                    info("message #3")
                }
            }
        }

        assertThat(ex).hasMessage(
            """
            Log entries do not match expectation:
            [✓] INFO | "message #1"
            [✗] INFO | [equal to ["message #3"]] >> actual ["message #2"]
            """.trimIndent()
        )
    }

    @TestFactory
    fun `size difference between actual and expected log throws assertion error`(): List<DynamicTest> {
        val log = logRecord(
            logEntry(message = "message #1"),
            logEntry(message = "message #2"),
            logEntry(message = "message #3")
        )

        return listOf(
            dynamicTest("too few messages") {
                assertThrows<AssertionError> {
                    assertThat(log) {
                        containsExactly { any("message #1"); any("message #2"); any("message #3"); any("message #4") }
                    }
                }
            },
            dynamicTest("too many messages") {
                assertThrows<AssertionError> {
                    assertThat(log) {
                        containsExactly { any("message #1"); any("message #2") }
                    }
                }
            }
        )
    }

    @Nested
    inner class TraceMessages : AbstractLevelMessage(TRACE) {
        override fun MessagesAssertionBlock.execute(message: String) = this.trace(message)
        override fun MessagesAssertionBlock.execute(vararg messageMatchers: MessageMatcher) =
            this.trace(*messageMatchers)
    }

    @Nested
    inner class DebugMessages : AbstractLevelMessage(DEBUG) {
        override fun MessagesAssertionBlock.execute(message: String) = this.debug(message)
        override fun MessagesAssertionBlock.execute(vararg messageMatchers: MessageMatcher) =
            this.debug(*messageMatchers)
    }

    @Nested
    inner class InfoMessages : AbstractLevelMessage(INFO) {
        override fun MessagesAssertionBlock.execute(message: String) = this.info(message)
        override fun MessagesAssertionBlock.execute(vararg messageMatchers: MessageMatcher) =
            this.info(*messageMatchers)
    }

    @Nested
    inner class WarnMessages : AbstractLevelMessage(WARN) {
        override fun MessagesAssertionBlock.execute(message: String) = this.warn(message)
        override fun MessagesAssertionBlock.execute(vararg messageMatchers: MessageMatcher) =
            this.warn(*messageMatchers)
    }

    @Nested
    inner class ErrorMessages : AbstractLevelMessage(ERROR) {
        override fun MessagesAssertionBlock.execute(message: String) = this.error(message)
        override fun MessagesAssertionBlock.execute(vararg messageMatchers: MessageMatcher) =
            this.error(*messageMatchers)
    }

    abstract inner class AbstractLevelMessage(private val level: LogLevel) {

        @Test
        fun `matches entries with expected log level`() {
            val log = logRecord(logEntry(level = level))
            assertThat(log) { containsExactly { execute("message") } }
        }

        @TestFactory
        fun `does not match entries with different log level`() = LogLevel.values()
            .filter { it != level }
            .map { example ->
                dynamicTest("$example") {
                    assertThrows<AssertionError> {
                        val log = logRecord(logEntry(level = example))
                        assertThat(log) { containsExactly { execute("message") } }
                    }
                }
            }

        @Test
        fun `matches any message when no message matchers are provided`() {
            val log = logRecord(logEntry(level = level))
            assertThat(log) { containsExactly { execute() } }
        }

        @Test
        fun `matches message when provided message matchers all match`() {
            val log = logRecord(logEntry(level = level, message = "hello world!"))
            assertThat(log) { containsExactly { execute(startsWith("hello"), endsWith("world!")) } }
        }

        @Test
        fun `does not match message when at least one provided message matcher does not match`() {
            val log = logRecord(logEntry(level = level, message = "hello world!"))
            assertThrows<AssertionError> {
                assertThat(log) { containsExactly { execute(endsWith("world")) } }
            }
        }

        abstract fun MessagesAssertionBlock.execute(message: String)
        abstract fun MessagesAssertionBlock.execute(vararg messageMatchers: MessageMatcher)

    }

    @Nested
    @DisplayName("any message matchers are asserted correctly")
    inner class AnyMessageMatcher {

        @ParameterizedTest
        @EnumSource(LogLevel::class)
        fun `any matcher matches all log levels`(logLevel: LogLevel) {
            assertThat(logRecord(logEntry(level = logLevel))) { containsExactly { any("message") } }
        }

        @ParameterizedTest
        @EnumSource(LogLevel::class)
        fun `any without matchers matches any message`(logLevel: LogLevel) {
            val log = logRecord(logEntry(level = logLevel))
            assertThat(log) { containsExactly { any() } }
        }

        @ParameterizedTest
        @EnumSource(LogLevel::class)
        fun `any with custom matchers matches matching entries`(logLevel: LogLevel) {
            val log = logRecord(logEntry(level = logLevel, message = "hello world!"))
            assertThat(log) { containsExactly { any(startsWith("hello"), endsWith("world!")) } }
        }

    }

    @Test
    fun `any matcher matches all log levels and messages`() {
        val log = logRecord(
            logEntry(level = INFO, message = "start"),

            logEntry(level = TRACE, message = "trace message"),
            logEntry(level = DEBUG, message = "debug message"),
            logEntry(level = INFO, message = "info message"),
            logEntry(level = WARN, message = "warn message"),
            logEntry(level = ERROR, message = "error message"),

            logEntry(level = INFO, message = "end")
        )
        assertThat(log) {
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
    @DisplayName("different message matchers work as expected")
    inner class MatcherVariants {

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
