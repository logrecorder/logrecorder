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
package info.novatec.testit.logrecorder.jul.junit5

import info.novatec.testit.logrecorder.api.LogEntry
import info.novatec.testit.logrecorder.api.LogLevel
import info.novatec.testit.logrecorder.api.LogRecord
import java.util.logging.Level
import java.util.logging.Logger
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test


internal class JulRecorderExtensionTest {

    private val customLogger = Logger.getLogger("custom-logger")

    private val testServiceA = TestServiceA()
    private val testServiceB = TestServiceB()

    @BeforeEach
    fun logSomethingBeforeTest() {
        testServiceA.logSomething()
        testServiceB.logSomething()
        customLogger.level = Level.WARNING
    }

    @RecordLoggers(TestServiceA::class, TestServiceB::class, names = ["custom-logger"])
    @Test
    fun `extension is registered and log messages are recorded`(log: LogRecord) {
        Assertions.assertThat(log.entries).isEmpty()

        testServiceA.logSomething()
        Assertions.assertThat(log.entries).containsExactly(
            LogEntry(LogRecord.logger(TestServiceA::class), LogLevel.TRACE, "trace message a"),
            LogEntry(LogRecord.logger(TestServiceA::class), LogLevel.DEBUG, "debug message a"),
            LogEntry(LogRecord.logger(TestServiceA::class), LogLevel.INFO, "info message a"),
            LogEntry(LogRecord.logger(TestServiceA::class), LogLevel.WARN, "warn message a"),
            LogEntry(LogRecord.logger(TestServiceA::class), LogLevel.ERROR, "error message a")
        )

        testServiceB.logSomething()
        Assertions.assertThat(log.entries).containsExactly(
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

        customLogger.finer("trace message c")
        customLogger.fine("debug message c")
        customLogger.info("info message c")
        customLogger.warning("warn message c")
        customLogger.severe("error message c")

        Assertions.assertThat(log.entries).containsExactly(
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

    @RecordLoggers(TestServiceA::class)
    @Test
    fun `extension is registered and log messages are recorded from ServiceA`(log: LogRecord) {
        Assertions.assertThat(log.entries).isEmpty()

        testServiceA.logSomething()
        Assertions.assertThat(log.entries).containsExactly(
            LogEntry(LogRecord.logger(TestServiceA::class), LogLevel.TRACE, "trace message a"),
            LogEntry(LogRecord.logger(TestServiceA::class), LogLevel.DEBUG, "debug message a"),
            LogEntry(LogRecord.logger(TestServiceA::class), LogLevel.INFO, "info message a"),
            LogEntry(LogRecord.logger(TestServiceA::class), LogLevel.WARN, "warn message a"),
            LogEntry(LogRecord.logger(TestServiceA::class), LogLevel.ERROR, "error message a")
        )

        testServiceB.logSomething()
        Assertions.assertThat(log.entries).containsExactly(
            LogEntry(LogRecord.logger(TestServiceA::class), LogLevel.TRACE, "trace message a"),
            LogEntry(LogRecord.logger(TestServiceA::class), LogLevel.DEBUG, "debug message a"),
            LogEntry(LogRecord.logger(TestServiceA::class), LogLevel.INFO, "info message a"),
            LogEntry(LogRecord.logger(TestServiceA::class), LogLevel.WARN, "warn message a"),
            LogEntry(LogRecord.logger(TestServiceA::class), LogLevel.ERROR, "error message a")
        )

        customLogger.finer("trace message c")
        customLogger.fine("debug message c")
        customLogger.info("info message c")
        customLogger.warning("warn message c")
        customLogger.severe("error message c")

        Assertions.assertThat(log.entries).containsExactly(
            LogEntry(LogRecord.logger(TestServiceA::class), LogLevel.TRACE, "trace message a"),
            LogEntry(LogRecord.logger(TestServiceA::class), LogLevel.DEBUG, "debug message a"),
            LogEntry(LogRecord.logger(TestServiceA::class), LogLevel.INFO, "info message a"),
            LogEntry(LogRecord.logger(TestServiceA::class), LogLevel.WARN, "warn message a"),
            LogEntry(LogRecord.logger(TestServiceA::class), LogLevel.ERROR, "error message a")
        )
    }

}

class TestServiceA {
    private val log = Logger.getLogger(TestServiceA::class.java.name)
    fun logSomething() {
        log.finer("trace message a")
        log.fine("debug message a")
        log.info("info message a")
        log.warning("warn message a")
        log.severe("error message a")
    }
}

class TestServiceB {
    private val log = Logger.getLogger(TestServiceB::class.java.name)
    fun logSomething() {
        log.finer("trace message b")
        log.fine("debug message b")
        log.info("info message b")
        log.warning("warn message b")
        log.severe("error message b")
    }
}
