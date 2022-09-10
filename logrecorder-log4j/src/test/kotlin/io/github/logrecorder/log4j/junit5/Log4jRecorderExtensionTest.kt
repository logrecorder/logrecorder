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
package io.github.logrecorder.log4j.junit5

import io.github.logrecorder.api.LogEntry
import io.github.logrecorder.api.LogLevel
import io.github.logrecorder.api.LogRecord
import io.github.logrecorder.log4j.util.TestServiceA
import io.github.logrecorder.log4j.util.TestServiceB
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.ThreadContext
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class Log4jRecorderExtensionTest {

    private val customLogger = LogManager.getLogger("custom-logger")

    private val testServiceA = TestServiceA()
    private val testServiceB = TestServiceB()

    @BeforeEach
    fun logSomethingBeforeTest() {
        testServiceA.logSomething()
        testServiceB.logSomething()
    }

    @AfterEach
    fun cleanup() {
        ThreadContext.clearAll()
    }

    @Test
    @RecordLoggers(TestServiceA::class, TestServiceB::class, names = ["custom-logger"])
    fun `extension is registered and log messages are recorded`(log: LogRecord) {
        assertThat(log.entries).isEmpty()

        testServiceA.logSomething()
        assertThat(log.entries).containsExactly(
            LogEntry(LogRecord.logger(TestServiceA::class), LogLevel.TRACE, "trace message a"),
            LogEntry(LogRecord.logger(TestServiceA::class), LogLevel.DEBUG, "debug message a"),
            LogEntry(LogRecord.logger(TestServiceA::class), LogLevel.INFO, "info message a"),
            LogEntry(LogRecord.logger(TestServiceA::class), LogLevel.WARN, "warn message a"),
            LogEntry(LogRecord.logger(TestServiceA::class), LogLevel.ERROR, "error message a")
        )

        testServiceB.logSomething()
        assertThat(log.entries).containsExactly(
            LogEntry(LogRecord.logger(TestServiceA::class), LogLevel.TRACE, "trace message a"),
            LogEntry(LogRecord.logger(TestServiceA::class), LogLevel.DEBUG, "debug message a"),
            LogEntry(LogRecord.logger(TestServiceA::class), LogLevel.INFO, "info message a"),
            LogEntry(LogRecord.logger(TestServiceA::class), LogLevel.WARN, "warn message a"),
            LogEntry(LogRecord.logger(TestServiceA::class), LogLevel.ERROR, "error message a"),

            LogEntry(LogRecord.logger(TestServiceB::class), LogLevel.TRACE, "trace message b"),
            LogEntry(LogRecord.logger(TestServiceB::class), LogLevel.DEBUG, "debug message b"),
            LogEntry(LogRecord.logger(TestServiceB::class), LogLevel.INFO, "info message b"),
            LogEntry(LogRecord.logger(TestServiceB::class), LogLevel.WARN, "warn message b"),
            LogEntry(LogRecord.logger(TestServiceB::class), LogLevel.ERROR, "error message b")
        )

        customLogger.trace("trace message c")
        customLogger.debug("debug message c")
        customLogger.info("info message c")
        customLogger.warn("warn message c")
        customLogger.error("error message c")

        assertThat(log.entries).containsExactly(
            LogEntry(LogRecord.logger(TestServiceA::class), LogLevel.TRACE, "trace message a"),
            LogEntry(LogRecord.logger(TestServiceA::class), LogLevel.DEBUG, "debug message a"),
            LogEntry(LogRecord.logger(TestServiceA::class), LogLevel.INFO, "info message a"),
            LogEntry(LogRecord.logger(TestServiceA::class), LogLevel.WARN, "warn message a"),
            LogEntry(LogRecord.logger(TestServiceA::class), LogLevel.ERROR, "error message a"),

            LogEntry(LogRecord.logger(TestServiceB::class), LogLevel.TRACE, "trace message b"),
            LogEntry(LogRecord.logger(TestServiceB::class), LogLevel.DEBUG, "debug message b"),
            LogEntry(LogRecord.logger(TestServiceB::class), LogLevel.INFO, "info message b"),
            LogEntry(LogRecord.logger(TestServiceB::class), LogLevel.WARN, "warn message b"),
            LogEntry(LogRecord.logger(TestServiceB::class), LogLevel.ERROR, "error message b"),

            LogEntry("custom-logger", LogLevel.TRACE, "trace message c"),
            LogEntry("custom-logger", LogLevel.DEBUG, "debug message c"),
            LogEntry("custom-logger", LogLevel.INFO, "info message c"),
            LogEntry("custom-logger", LogLevel.WARN, "warn message c"),
            LogEntry("custom-logger", LogLevel.ERROR, "error message c")
        )
    }

    @Test
    @RecordLoggers(TestServiceA::class)
    fun `extension is registered and log messages are recorded from ServiceA`(log: LogRecord) {
        assertThat(log.entries).isEmpty()

        testServiceA.logSomething()
        assertThat(log.entries).containsExactly(
            LogEntry(LogRecord.logger(TestServiceA::class), LogLevel.TRACE, "trace message a"),
            LogEntry(LogRecord.logger(TestServiceA::class), LogLevel.DEBUG, "debug message a"),
            LogEntry(LogRecord.logger(TestServiceA::class), LogLevel.INFO, "info message a"),
            LogEntry(LogRecord.logger(TestServiceA::class), LogLevel.WARN, "warn message a"),
            LogEntry(LogRecord.logger(TestServiceA::class), LogLevel.ERROR, "error message a")
        )

        testServiceB.logSomething()
        assertThat(log.entries).containsExactly(
            LogEntry(LogRecord.logger(TestServiceA::class), LogLevel.TRACE, "trace message a"),
            LogEntry(LogRecord.logger(TestServiceA::class), LogLevel.DEBUG, "debug message a"),
            LogEntry(LogRecord.logger(TestServiceA::class), LogLevel.INFO, "info message a"),
            LogEntry(LogRecord.logger(TestServiceA::class), LogLevel.WARN, "warn message a"),
            LogEntry(LogRecord.logger(TestServiceA::class), LogLevel.ERROR, "error message a")
        )

        customLogger.trace("trace message c")
        customLogger.debug("debug message c")
        customLogger.info("info message c")
        customLogger.warn("warn message c")
        customLogger.error("error message c")

        assertThat(log.entries).containsExactly(
            LogEntry(LogRecord.logger(TestServiceA::class), LogLevel.TRACE, "trace message a"),
            LogEntry(LogRecord.logger(TestServiceA::class), LogLevel.DEBUG, "debug message a"),
            LogEntry(LogRecord.logger(TestServiceA::class), LogLevel.INFO, "info message a"),
            LogEntry(LogRecord.logger(TestServiceA::class), LogLevel.WARN, "warn message a"),
            LogEntry(LogRecord.logger(TestServiceA::class), LogLevel.ERROR, "error message a")
        )
    }

    @Test
    @RecordLoggers(TestServiceA::class)
    fun `MDC properties are recorded`(log: LogRecord) {
        ThreadContext.put("custom#1", "foo")
        ThreadContext.put("custom#2", "bar")
        testServiceA.logSingleInfo()
        ThreadContext.remove("custom#2")
        testServiceA.logSingleInfo()

        assertThat(log.entries).containsExactly(
            LogEntry(
                logger = LogRecord.logger(TestServiceA::class),
                level = LogLevel.INFO,
                message = "info message a",
                properties = mapOf(
                    "custom#1" to "foo",
                    "custom#2" to "bar"
                )
            ),
            LogEntry(
                logger = LogRecord.logger(TestServiceA::class),
                level = LogLevel.INFO,
                message = "info message a",
                properties = mapOf(
                    "custom#1" to "foo"
                )
            )
        )
    }

}
