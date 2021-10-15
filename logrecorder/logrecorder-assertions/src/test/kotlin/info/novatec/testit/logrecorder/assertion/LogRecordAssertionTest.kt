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
import org.junit.jupiter.params.provider.EnumSource.Mode.EXCLUDE

internal class LogRecordAssertionTest {

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
            containsInOrder {
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
            dynamicTest("to few messages") {
                assertThrows<AssertionError> {
                    assertThat(log) {
                        containsInOrder { any("message #1"); any("message #2"); any("message #3"); any("message #4") }
                    }
                }
            },
            dynamicTest("to many messages") {
                assertThrows<AssertionError> {
                    assertThat(log) {
                        containsInOrder { any("message #1"); any("message #2") }
                    }
                }
            }
        )
    }

    @Nested
    @DisplayName("TRACE messages are asserted correctly")
    inner class TraceMessage {

        @Test
        fun `trace matcher matches TRACE entries`() {
            val log = logWith(entry(level = TRACE))
            assertThat(log) { containsInOrder { trace("message") } }
        }

        @ParameterizedTest(name = "{0} entries")
        @EnumSource(LogLevel::class, mode = EXCLUDE, names = ["TRACE"])
        fun `trace matcher does not match`(logLevel: LogLevel) {
            assertThrows<AssertionError> {
                assertThat(logWith(entry(level = logLevel))) { containsInOrder { trace("message") } }
            }
        }

        @Test
        fun `trace without matchers matches any message`() {
            val log = logWith(entry(level = TRACE))
            assertThat(log) { containsInOrder { trace() } }
        }

        @Test
        fun `trace with custom matchers matches matching entries`() {
            val log = logWith(entry(level = TRACE, message = "hello world!"))
            assertThat(log) { containsInOrder { trace(startsWith("hello"), endsWith("world!")) } }
        }

    }

    @Nested
    @DisplayName("DEBUG messages are asserted correctly")
    inner class DebugMessage {

        @Test
        fun `debug matcher matches DEBUG entries`() {
            val log = logWith(entry(level = DEBUG))
            assertThat(log) { containsInOrder { debug("message") } }
        }

        @ParameterizedTest(name = "{0} entries")
        @EnumSource(LogLevel::class, mode = EXCLUDE, names = ["DEBUG"])
        fun `debug matcher does not match`(logLevel: LogLevel) {
            assertThrows<AssertionError> {
                assertThat(logWith(entry(level = logLevel))) { containsInOrder { debug("message") } }
            }
        }

        @Test
        fun `debug without matchers matches any message`() {
            val log = logWith(entry(level = DEBUG))
            assertThat(log) { containsInOrder { debug() } }
        }

        @Test
        fun `debug with custom matchers matches matching entries`() {
            val log = logWith(entry(level = DEBUG, message = "hello world!"))
            assertThat(log) { containsInOrder { debug(startsWith("hello"), endsWith("world!")) } }
        }

    }

    @Nested
    @DisplayName("INFO messages are asserted correctly")
    inner class InfoMessage {

        @Test
        fun `info matcher matches INFO entries`() {
            val log = logWith(entry(level = INFO))
            assertThat(log) { containsInOrder { info("message") } }
        }

        @ParameterizedTest(name = "{0} entries")
        @EnumSource(LogLevel::class, mode = EXCLUDE, names = ["INFO"])
        fun `info matcher does not match`(logLevel: LogLevel) {
            assertThrows<AssertionError> {
                assertThat(logWith(entry(level = logLevel))) { containsInOrder { info("message") } }
            }
        }

        @Test
        fun `info without matchers matches any message`() {
            val log = logWith(entry(level = INFO))
            assertThat(log) { containsInOrder { info() } }
        }

        @Test
        fun `info with custom matchers matches matching entries`() {
            val log = logWith(entry(level = INFO, message = "hello world!"))
            assertThat(log) { containsInOrder { info(startsWith("hello"), endsWith("world!")) } }
        }

    }

    @Nested
    @DisplayName("WARN messages are asserted correctly")
    inner class WarnMessage {

        @Test
        fun `warn matcher matches WARN entries`() {
            val log = logWith(entry(level = WARN))
            assertThat(log) { containsInOrder { warn("message") } }
        }

        @ParameterizedTest(name = "{0} entries")
        @EnumSource(LogLevel::class, mode = EXCLUDE, names = ["WARN"])
        fun `warn matcher does not match`(logLevel: LogLevel) {
            assertThrows<AssertionError> {
                assertThat(logWith(entry(level = logLevel))) { containsInOrder { warn("message") } }
            }
        }

        @Test
        fun `warn without matchers matches any message`() {
            val log = logWith(entry(level = WARN))
            assertThat(log) { containsInOrder { warn() } }
        }

        @Test
        fun `warn with custom matchers matches matching entries`() {
            val log = logWith(entry(level = WARN, message = "hello world!"))
            assertThat(log) { containsInOrder { warn(startsWith("hello"), endsWith("world!")) } }
        }

    }

    @Nested
    @DisplayName("ERROR messages are asserted correctly")
    inner class ErrorMessage {

        @Test
        fun `error matcher matches ERROR entries`() {
            val log = logWith(entry(level = ERROR))
            assertThat(log) { containsInOrder { error("message") } }
        }

        @ParameterizedTest(name = "{0} entries")
        @EnumSource(LogLevel::class, mode = EXCLUDE, names = ["ERROR"])
        fun `error matcher does not match`(logLevel: LogLevel) {
            assertThrows<AssertionError> {
                assertThat(logWith(entry(level = logLevel))) { containsInOrder { error("message") } }
            }
        }

        @Test
        fun `error without matchers matches any message`() {
            val log = logWith(entry(level = ERROR))
            assertThat(log) { containsInOrder { error() } }
        }

        @Test
        fun `error with custom matchers matches matching entries`() {
            val log = logWith(entry(level = ERROR, message = "hello world!"))
            assertThat(log) { containsInOrder { error(startsWith("hello"), endsWith("world!")) } }
        }

    }

    @Nested
    @DisplayName("any message matchers are asserted correctly")
    inner class AnyMessageMatcher {

        @ParameterizedTest
        @EnumSource(LogLevel::class)
        fun `any matcher matches all log levels`(logLevel: LogLevel) {
            assertThat(logWith(entry(level = logLevel))) { containsInOrder { any("message") } }
        }

        @ParameterizedTest
        @EnumSource(LogLevel::class)
        fun `any without matchers matches any message`(logLevel: LogLevel) {
            val log = logWith(entry(level = logLevel))
            assertThat(log) { containsInOrder { any() } }
        }

        @ParameterizedTest
        @EnumSource(LogLevel::class)
        fun `any with custom matchers matches matching entries`(logLevel: LogLevel) {
            val log = logWith(entry(level = logLevel, message = "hello world!"))
            assertThat(log) { containsInOrder { any(startsWith("hello"), endsWith("world!")) } }
        }

    }

    @Test
    fun `something matcher matches all log levels and messages`() {
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
            containsInOrder {
                info("start")
                something()
                something()
                something()
                something()
                something()
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
                assertThat(log) { containsInOrder { any(equalTo("Foo bar XUR")) } }
            }

            @Test
            fun `matching is done case sensitive`() {
                assertThrows<AssertionError> {
                    assertThat(log) { containsInOrder { any(equalTo("foo bar xur")) } }
                }
            }

            @Test
            fun `not matching message throws assertion error`() {
                assertThrows<AssertionError> {
                    assertThat(log) { containsInOrder { any(equalTo("something else")) } }
                }
            }

        }

        @Nested
        @DisplayName("matches(..)")
        inner class Matches {

            @Test
            fun `message matching the RegEx is OK`() {
                assertThat(log) { containsInOrder { any(matches("Foo.+")) } }
            }

            @Test
            fun `matching is done case sensitive`() {
                assertThrows<AssertionError> {
                    assertThat(log) { containsInOrder { any(matches("foo.+")) } }
                }
            }

            @Test
            fun `message not matching the RegEx throws assertion error`() {
                assertThrows<AssertionError> {
                    assertThat(log) { containsInOrder { any(matches("Bar.+")) } }
                }
            }

        }

        @Nested
        @DisplayName("contains(..)")
        inner class Contains {

            @Test
            fun `message containing all supplied parts is OK`() {
                assertThat(log) { containsInOrder { any(contains("Foo", "XUR")) } }
            }

            @Test
            fun `message containing all supplied parts in any order is OK`() {
                assertThat(log) { containsInOrder { any(contains("XUR", "Foo")) } }
            }

            @Test
            fun `matching is done case sensitive`() {
                assertThrows<AssertionError> {
                    assertThat(log) { containsInOrder { any(contains("foo", "xur")) } }
                }
            }

            @Test
            fun `message containing non of the parts throws assertion error`() {
                assertThrows<AssertionError> {
                    assertThat(log) { containsInOrder { any(contains("message")) } }
                }
            }

            @Test
            fun `message containing only some of the parts throws assertion error`() {
                assertThrows<AssertionError> {
                    assertThat(log) { containsInOrder { any(contains("Foo", "message")) } }
                }
            }

        }

        @Nested
        @DisplayName("containsInOrder(..)")
        inner class ContainsInOrder {

            @Test
            fun `message containing all supplied parts in order is OK`() {
                assertThat(log) { containsInOrder { any(containsInOrder("Foo", "XUR")) } }
            }

            @Test
            fun `message containing all supplied parts but not in order throws assertion error`() {
                assertThrows<AssertionError> {
                    assertThat(log) { containsInOrder { any(containsInOrder("XUR", "Foo")) } }
                }
            }

            @Test
            fun `matching is done case sensitive`() {
                assertThrows<AssertionError> {
                    assertThat(log) { containsInOrder { any(containsInOrder("foo", "xur")) } }
                }
            }

            @Test
            fun `message containing non of the parts throws assertion error`() {
                assertThrows<AssertionError> {
                    assertThat(log) { containsInOrder { any(containsInOrder("message")) } }
                }
            }

            @Test
            fun `message containing only some of the parts throws assertion error`() {
                assertThrows<AssertionError> {
                    assertThat(log) { containsInOrder { any(containsInOrder("Foo", "message")) } }
                }
            }

        }

        @Nested
        @DisplayName("startsWith(..)")
        inner class StartsWith {

            @Test
            fun `message starting with prefix is OK`() {
                assertThat(log) { containsInOrder { any(startsWith("Foo ")) } }
            }

            @Test
            fun `message not starting with prefix throws assertion error`() {
                assertThrows<AssertionError> {
                    assertThat(log) { containsInOrder { any(startsWith("message")) } }
                }
            }

            @Test
            fun `matching is done case sensitive`() {
                assertThrows<AssertionError> {
                    assertThat(log) { containsInOrder { any(startsWith("foo ")) } }
                }
            }

        }

        @Nested
        @DisplayName("endsWith(..)")
        inner class EndsWith {

            @Test
            fun `message starting with prefix is OK`() {
                assertThat(log) { containsInOrder { any(endsWith(" XUR")) } }
            }

            @Test
            fun `message not starting with prefix throws assertion error`() {
                assertThrows<AssertionError> {
                    assertThat(log) { containsInOrder { any(endsWith("message")) } }
                }
            }

            @Test
            fun `matching is done case sensitive`() {
                assertThrows<AssertionError> {
                    assertThat(log) { containsInOrder { any(endsWith(" xur")) } }
                }
            }

        }

    }

    fun logWith(vararg entries: LogEntry): LogRecord = TestLogRecord(listOf(*entries))
    fun entry(logger: String = "logger", level: LogLevel = INFO, message: String = "message") =
        LogEntry(logger, level, message)

}
