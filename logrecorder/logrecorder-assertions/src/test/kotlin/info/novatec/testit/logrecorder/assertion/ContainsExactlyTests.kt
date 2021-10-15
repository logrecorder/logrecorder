package info.novatec.testit.logrecorder.assertion

import info.novatec.testit.logrecorder.api.LogEntry
import info.novatec.testit.logrecorder.api.LogLevel
import info.novatec.testit.logrecorder.api.LogLevel.*
import info.novatec.testit.logrecorder.api.LogRecord
import info.novatec.testit.logrecorder.assertion.LogRecordAssertion.Companion.assertThat
import org.junit.jupiter.api.*
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource

internal class ContainsExactlyTests {

    @Test
    fun `reference equality check`() {
        val log = logWith(
            entry(level = TRACE, message = "trace message"),
            entry(level = DEBUG, message = "debug message"),
            entry(level = INFO, message = "info message"),
            entry(level = WARN, message = "warn message"),
            entry(level = ERROR, message = "error message")
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

    @TestFactory
    fun `size difference between actual and expected log throws assertion error`(): List<DynamicTest> {
        val log = logWith(
            entry(message = "message #1"),
            entry(message = "message #2"),
            entry(message = "message #3")
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
        override fun ContainsExactly.execute(message: String) = this.trace(message)
        override fun ContainsExactly.execute(vararg messageMatchers: MessageMatcher) =
            this.trace(*messageMatchers)
    }

    @Nested
    inner class DebugMessages : AbstractLevelMessage(DEBUG) {
        override fun ContainsExactly.execute(message: String) = this.debug(message)
        override fun ContainsExactly.execute(vararg messageMatchers: MessageMatcher) =
            this.debug(*messageMatchers)
    }

    @Nested
    inner class InfoMessages : AbstractLevelMessage(INFO) {
        override fun ContainsExactly.execute(message: String) = this.info(message)
        override fun ContainsExactly.execute(vararg messageMatchers: MessageMatcher) =
            this.info(*messageMatchers)
    }

    @Nested
    inner class WarnMessages : AbstractLevelMessage(WARN) {
        override fun ContainsExactly.execute(message: String) = this.warn(message)
        override fun ContainsExactly.execute(vararg messageMatchers: MessageMatcher) =
            this.warn(*messageMatchers)
    }

    @Nested
    inner class ErrorMessages : AbstractLevelMessage(ERROR) {
        override fun ContainsExactly.execute(message: String) = this.error(message)
        override fun ContainsExactly.execute(vararg messageMatchers: MessageMatcher) =
            this.error(*messageMatchers)
    }

    abstract inner class AbstractLevelMessage(private val level: LogLevel) {

        @Test
        fun `matches entries with expected log level`() {
            val log = logWith(entry(level = level))
            assertThat(log) { containsExactly { execute("message") } }
        }

        @TestFactory
        fun `does not match entries with different log level`() = LogLevel.values()
            .filter { it != level }
            .map { example ->
                dynamicTest("$example") {
                    assertThrows<AssertionError> {
                        val log = logWith(entry(level = example))
                        assertThat(log) { containsExactly { execute("message") } }
                    }
                }
            }

        @Test
        fun `matches any message when no message matchers are provided`() {
            val log = logWith(entry(level = level))
            assertThat(log) { containsExactly { execute() } }
        }

        @Test
        fun `matches message when provided message matchers all match`() {
            val log = logWith(entry(level = level, message = "hello world!"))
            assertThat(log) { containsExactly { execute(startsWith("hello"), endsWith("world!")) } }
        }

        @Test
        fun `does not match message when at least one provided message matcher does not match`() {
            val log = logWith(entry(level = level, message = "hello world!"))
            assertThrows<AssertionError> {
                assertThat(log) { containsExactly { execute(endsWith("world")) } }
            }
        }

        abstract fun ContainsExactly.execute(message: String)
        abstract fun ContainsExactly.execute(vararg messageMatchers: MessageMatcher)

    }

    @Nested
    @DisplayName("any message matchers are asserted correctly")
    inner class AnyMessageMatcher {

        @ParameterizedTest
        @EnumSource(LogLevel::class)
        fun `any matcher matches all log levels`(logLevel: LogLevel) {
            assertThat(logWith(entry(level = logLevel))) { containsExactly { any("message") } }
        }

        @ParameterizedTest
        @EnumSource(LogLevel::class)
        fun `any without matchers matches any message`(logLevel: LogLevel) {
            val log = logWith(entry(level = logLevel))
            assertThat(log) { containsExactly { any() } }
        }

        @ParameterizedTest
        @EnumSource(LogLevel::class)
        fun `any with custom matchers matches matching entries`(logLevel: LogLevel) {
            val log = logWith(entry(level = logLevel, message = "hello world!"))
            assertThat(log) { containsExactly { any(startsWith("hello"), endsWith("world!")) } }
        }

    }

    @Test
    fun `any matcher matches all log levels and messages`() {
        val log = logWith(
            entry(level = INFO, message = "start"),

            entry(level = TRACE, message = "trace message"),
            entry(level = DEBUG, message = "debug message"),
            entry(level = INFO, message = "info message"),
            entry(level = WARN, message = "warn message"),
            entry(level = ERROR, message = "error message"),

            entry(level = INFO, message = "end")
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

        val log = logWith(entry(message = "Foo bar XUR"))

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

    fun logWith(vararg entries: LogEntry): LogRecord = TestLogRecord(listOf(*entries))
    fun entry(logger: String = "logger", level: LogLevel = INFO, message: String = "message") =
        LogEntry(logger, level, message)

}
