package info.novatec.testit.logrecorder.logback.junit5

import info.novatec.testit.logrecorder.api.LogEntry
import info.novatec.testit.logrecorder.api.LogLevel
import info.novatec.testit.logrecorder.api.LogRecord
import info.novatec.testit.logrecorder.api.LogRecord.Companion.logger
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory

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
            LogEntry(logger(TestServiceA::class), LogLevel.TRACE, "trace message a"),
            LogEntry(logger(TestServiceA::class), LogLevel.DEBUG, "debug message a"),
            LogEntry(logger(TestServiceA::class), LogLevel.INFO, "info message a"),
            LogEntry(logger(TestServiceA::class), LogLevel.WARN, "warn message a"),
            LogEntry(logger(TestServiceA::class), LogLevel.ERROR, "error message a")
        )

        testServiceB.logSomething()
        assertThat(log.entries).containsExactly(
            LogEntry(logger(TestServiceA::class), LogLevel.TRACE, "trace message a"),
            LogEntry(logger(TestServiceA::class), LogLevel.DEBUG, "debug message a"),
            LogEntry(logger(TestServiceA::class), LogLevel.INFO, "info message a"),
            LogEntry(logger(TestServiceA::class), LogLevel.WARN, "warn message a"),
            LogEntry(logger(TestServiceA::class), LogLevel.ERROR, "error message a"),

            LogEntry(logger(TestServiceB::class), LogLevel.TRACE, "trace message b"),
            LogEntry(logger(TestServiceB::class), LogLevel.DEBUG, "debug message b"),
            LogEntry(logger(TestServiceB::class), LogLevel.INFO, "info message b"),
            LogEntry(logger(TestServiceB::class), LogLevel.WARN, "warn message b"),
            LogEntry(logger(TestServiceB::class), LogLevel.ERROR, "error message b")
        )

        customLogger.trace("trace message c")
        customLogger.debug("debug message c")
        customLogger.info("info message c")
        customLogger.warn("warn message c")
        customLogger.error("error message c")

        assertThat(log.entries).containsExactly(
            LogEntry(logger(TestServiceA::class), LogLevel.TRACE, "trace message a"),
            LogEntry(logger(TestServiceA::class), LogLevel.DEBUG, "debug message a"),
            LogEntry(logger(TestServiceA::class), LogLevel.INFO, "info message a"),
            LogEntry(logger(TestServiceA::class), LogLevel.WARN, "warn message a"),
            LogEntry(logger(TestServiceA::class), LogLevel.ERROR, "error message a"),

            LogEntry(logger(TestServiceB::class), LogLevel.TRACE, "trace message b"),
            LogEntry(logger(TestServiceB::class), LogLevel.DEBUG, "debug message b"),
            LogEntry(logger(TestServiceB::class), LogLevel.INFO, "info message b"),
            LogEntry(logger(TestServiceB::class), LogLevel.WARN, "warn message b"),
            LogEntry(logger(TestServiceB::class), LogLevel.ERROR, "error message b"),

            LogEntry("custom-logger", LogLevel.TRACE, "trace message c"),
            LogEntry("custom-logger", LogLevel.DEBUG, "debug message c"),
            LogEntry("custom-logger", LogLevel.INFO, "info message c"),
            LogEntry("custom-logger", LogLevel.WARN, "warn message c"),
            LogEntry("custom-logger", LogLevel.ERROR, "error message c")
        )
    }

    @RecordLoggers(TestServiceA::class)
    @Test fun `extension is registered and log messages are recorded only for TestServiceA`(log: LogRecord) {
        assertThat(log.entries).isEmpty()

        testServiceA.logSomething()
        assertThat(log.entries).containsExactly(
            LogEntry(logger(TestServiceA::class), LogLevel.TRACE, "trace message a"),
            LogEntry(logger(TestServiceA::class), LogLevel.DEBUG, "debug message a"),
            LogEntry(logger(TestServiceA::class), LogLevel.INFO, "info message a"),
            LogEntry(logger(TestServiceA::class), LogLevel.WARN, "warn message a"),
            LogEntry(logger(TestServiceA::class), LogLevel.ERROR, "error message a")
        )

        testServiceB.logSomething()
        assertThat(log.entries).containsExactly(
            LogEntry(logger(TestServiceA::class), LogLevel.TRACE, "trace message a"),
            LogEntry(logger(TestServiceA::class), LogLevel.DEBUG, "debug message a"),
            LogEntry(logger(TestServiceA::class), LogLevel.INFO, "info message a"),
            LogEntry(logger(TestServiceA::class), LogLevel.WARN, "warn message a"),
            LogEntry(logger(TestServiceA::class), LogLevel.ERROR, "error message a")
        )

        customLogger.trace("trace message c")
        customLogger.debug("debug message c")
        customLogger.info("info message c")
        customLogger.warn("warn message c")
        customLogger.error("error message c")

        assertThat(log.entries).containsExactly(
            LogEntry(logger(TestServiceA::class), LogLevel.TRACE, "trace message a"),
            LogEntry(logger(TestServiceA::class), LogLevel.DEBUG, "debug message a"),
            LogEntry(logger(TestServiceA::class), LogLevel.INFO, "info message a"),
            LogEntry(logger(TestServiceA::class), LogLevel.WARN, "warn message a"),
            LogEntry(logger(TestServiceA::class), LogLevel.ERROR, "error message a")
        )
    }

}

class TestServiceA {
    private val log = LoggerFactory.getLogger(javaClass)
    fun logSomething() {
        log.trace("trace message a")
        log.debug("debug message a")
        log.info("info message a")
        log.warn("warn message a")
        log.error("error message a")
    }
}

class TestServiceB {
    private val log = LoggerFactory.getLogger(javaClass)
    fun logSomething() {
        log.trace("trace message b")
        log.debug("debug message b")
        log.info("info message b")
        log.warn("warn message b")
        log.error("error message b")
    }
}
