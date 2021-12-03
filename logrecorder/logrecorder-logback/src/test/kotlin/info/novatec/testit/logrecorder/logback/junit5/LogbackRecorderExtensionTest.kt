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
package info.novatec.testit.logrecorder.logback.junit5

import info.novatec.testit.logrecorder.api.LogEntry
import info.novatec.testit.logrecorder.api.LogLevel
import info.novatec.testit.logrecorder.api.LogRecord
import info.novatec.testit.logrecorder.api.LogRecord.Companion.logger
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.slf4j.Marker
import org.slf4j.helpers.BasicMarker
import org.slf4j.helpers.BasicMarkerFactory

internal class LogbackRecorderExtensionTest {

    val customLogger = LoggerFactory.getLogger("custom-logger")

    val testServiceA = TestServiceA()
    val testServiceB = TestServiceB()

    @BeforeEach fun logSomethingBeforeTest() {
        testServiceA.logSomething()
        testServiceB.logSomething()
    }

    @RecordLoggers(TestServiceA::class, TestServiceB::class, names = ["custom-logger"])
    @Test fun `extension is registered and log messages are recorded`(log: LogRecord) {
        assertThat(log.entries).isEmpty()

        testServiceA.logSomething()
        assertThat(log.entries).containsExactly(
            LogEntry(logger(TestServiceA::class), LogLevel.TRACE, "trace message a", "marker a"),
            LogEntry(logger(TestServiceA::class), LogLevel.DEBUG, "debug message a", "marker a"),
            LogEntry(logger(TestServiceA::class), LogLevel.INFO, "info message a", "marker a"),
            LogEntry(logger(TestServiceA::class), LogLevel.WARN, "warn message a", "marker a"),
            LogEntry(logger(TestServiceA::class), LogLevel.ERROR, "error message a", "marker a")
        )

        testServiceB.logSomething()
        assertThat(log.entries).containsExactly(
            LogEntry(logger(TestServiceA::class), LogLevel.TRACE, "trace message a", "marker a"),
            LogEntry(logger(TestServiceA::class), LogLevel.DEBUG, "debug message a", "marker a"),
            LogEntry(logger(TestServiceA::class), LogLevel.INFO, "info message a", "marker a"),
            LogEntry(logger(TestServiceA::class), LogLevel.WARN, "warn message a", "marker a"),
            LogEntry(logger(TestServiceA::class), LogLevel.ERROR, "error message a", "marker a"),

            LogEntry(logger(TestServiceB::class), LogLevel.TRACE, "trace message b", "marker b"),
            LogEntry(logger(TestServiceB::class), LogLevel.DEBUG, "debug message b", "marker b"),
            LogEntry(logger(TestServiceB::class), LogLevel.INFO, "info message b", "marker b"),
            LogEntry(logger(TestServiceB::class), LogLevel.WARN, "warn message b", "marker b"),
            LogEntry(logger(TestServiceB::class), LogLevel.ERROR, "error message b", "marker b")
        )

        val marker = BasicMarkerFactory().getMarker("marker c")
        customLogger.trace(marker, "trace message c")
        customLogger.debug(marker, "debug message c")
        customLogger.info(marker, "info message c")
        customLogger.warn(marker, "warn message c")
        customLogger.error(marker, "error message c")

        assertThat(log.entries).containsExactly(
            LogEntry(logger(TestServiceA::class), LogLevel.TRACE, "trace message a", "marker a"),
            LogEntry(logger(TestServiceA::class), LogLevel.DEBUG, "debug message a", "marker a"),
            LogEntry(logger(TestServiceA::class), LogLevel.INFO, "info message a", "marker a"),
            LogEntry(logger(TestServiceA::class), LogLevel.WARN, "warn message a", "marker a"),
            LogEntry(logger(TestServiceA::class), LogLevel.ERROR, "error message a", "marker a"),

            LogEntry(logger(TestServiceB::class), LogLevel.TRACE, "trace message b", "marker b"),
            LogEntry(logger(TestServiceB::class), LogLevel.DEBUG, "debug message b", "marker b"),
            LogEntry(logger(TestServiceB::class), LogLevel.INFO, "info message b", "marker b"),
            LogEntry(logger(TestServiceB::class), LogLevel.WARN, "warn message b", "marker b"),
            LogEntry(logger(TestServiceB::class), LogLevel.ERROR, "error message b", "marker b"),

            LogEntry("custom-logger", LogLevel.TRACE, "trace message c", "marker c"),
            LogEntry("custom-logger", LogLevel.DEBUG, "debug message c", "marker c"),
            LogEntry("custom-logger", LogLevel.INFO, "info message c", "marker c"),
            LogEntry("custom-logger", LogLevel.WARN, "warn message c", "marker c"),
            LogEntry("custom-logger", LogLevel.ERROR, "error message c", "marker c")
        )
    }

    @RecordLoggers(TestServiceA::class)
    @Test fun `extension is registered and log messages are recorded only for TestServiceA`(log: LogRecord) {
        assertThat(log.entries).isEmpty()

        testServiceA.logSomething()
        assertThat(log.entries).containsExactly(
            LogEntry(logger(TestServiceA::class), LogLevel.TRACE, "trace message a", "marker a"),
            LogEntry(logger(TestServiceA::class), LogLevel.DEBUG, "debug message a", "marker a"),
            LogEntry(logger(TestServiceA::class), LogLevel.INFO, "info message a", "marker a"),
            LogEntry(logger(TestServiceA::class), LogLevel.WARN, "warn message a", "marker a"),
            LogEntry(logger(TestServiceA::class), LogLevel.ERROR, "error message a", "marker a")
        )

        testServiceB.logSomething()
        assertThat(log.entries).containsExactly(
            LogEntry(logger(TestServiceA::class), LogLevel.TRACE, "trace message a", "marker a"),
            LogEntry(logger(TestServiceA::class), LogLevel.DEBUG, "debug message a", "marker a"),
            LogEntry(logger(TestServiceA::class), LogLevel.INFO, "info message a", "marker a"),
            LogEntry(logger(TestServiceA::class), LogLevel.WARN, "warn message a", "marker a"),
            LogEntry(logger(TestServiceA::class), LogLevel.ERROR, "error message a", "marker a")
        )

        customLogger.trace("trace message c")
        customLogger.debug("debug message c")
        customLogger.info("info message c")
        customLogger.warn("warn message c")
        customLogger.error("error message c")

        assertThat(log.entries).containsExactly(
            LogEntry(logger(TestServiceA::class), LogLevel.TRACE, "trace message a", "marker a"),
            LogEntry(logger(TestServiceA::class), LogLevel.DEBUG, "debug message a", "marker a"),
            LogEntry(logger(TestServiceA::class), LogLevel.INFO, "info message a", "marker a"),
            LogEntry(logger(TestServiceA::class), LogLevel.WARN, "warn message a", "marker a"),
            LogEntry(logger(TestServiceA::class), LogLevel.ERROR, "error message a", "marker a")
        )
    }

}

class TestServiceA {
    private val log = LoggerFactory.getLogger(javaClass)
    private val marker = BasicMarkerFactory().getMarker("marker a")

    fun logSomething() {
        log.trace(marker, "trace message a")
        log.debug(marker, "debug message a")
        log.info(marker, "info message a")
        log.warn(marker, "warn message a")
        log.error(marker, "error message a")
    }
}

class TestServiceB {
    private val log = LoggerFactory.getLogger(javaClass)
    private val marker = BasicMarkerFactory().getMarker("marker b")

    fun logSomething() {
        log.trace(marker, "trace message b")
        log.debug(marker, "debug message b")
        log.info(marker, "info message b")
        log.warn(marker, "warn message b")
        log.error(marker, "error message b")
    }
}
