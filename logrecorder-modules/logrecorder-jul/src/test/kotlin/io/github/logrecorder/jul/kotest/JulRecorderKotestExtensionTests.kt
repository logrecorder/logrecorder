package io.github.logrecorder.jul.kotest

import io.github.logrecorder.api.LogEntry
import io.github.logrecorder.api.LogLevel
import io.github.logrecorder.api.LogRecord.Companion.logger
import io.github.logrecorder.jul.util.TestServiceA
import io.github.logrecorder.jul.util.TestServiceB
import io.github.logrecorder.kotest.logRecord
import io.github.logrecorder.kotest.recordLogs
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactly
import java.util.logging.Level
import java.util.logging.Logger

class JulRecorderKotestExtensionTests : FunSpec({
    val customLogger = Logger.getLogger("custom-logger")

    val testServiceA = TestServiceA()
    val testServiceB = TestServiceB()

    beforeEach {
        testServiceA.logSomething()
        testServiceB.logSomething()
        customLogger.level = Level.WARNING
    }

    test("log messages are recorded").config(
        extensions = listOf(recordLogs(TestServiceA::class, TestServiceB::class, names = arrayOf("custom-logger")))
    ) {
        logRecord.entries.shouldBeEmpty()

        testServiceA.logSomething()
        logRecord.entries.shouldContainExactly(
            LogEntry(logger(TestServiceA::class), LogLevel.TRACE, "trace message a"),
            LogEntry(logger(TestServiceA::class), LogLevel.DEBUG, "debug message a"),
            LogEntry(logger(TestServiceA::class), LogLevel.INFO, "info message a"),
            LogEntry(logger(TestServiceA::class), LogLevel.WARN, "warn message a"),
            LogEntry(logger(TestServiceA::class), LogLevel.ERROR, "error message a")
        )

        testServiceB.logSomething()
        logRecord.entries.shouldContainExactly(
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

        customLogger.finer("trace message c")
        customLogger.fine("debug message c")
        customLogger.info("info message c")
        customLogger.warning("warn message c")
        customLogger.severe("error message c")

        logRecord.entries.shouldContainExactly(
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

    test("log messages are recorded from ServiceA").config(extensions = listOf(recordLogs(TestServiceA::class))) {
        logRecord.entries.shouldBeEmpty()

        testServiceA.logSomething()
        logRecord.entries.shouldContainExactly(
            LogEntry(logger(TestServiceA::class), LogLevel.TRACE, "trace message a"),
            LogEntry(logger(TestServiceA::class), LogLevel.DEBUG, "debug message a"),
            LogEntry(logger(TestServiceA::class), LogLevel.INFO, "info message a"),
            LogEntry(logger(TestServiceA::class), LogLevel.WARN, "warn message a"),
            LogEntry(logger(TestServiceA::class), LogLevel.ERROR, "error message a")
        )

        testServiceB.logSomething()
        logRecord.entries.shouldContainExactly(
            LogEntry(logger(TestServiceA::class), LogLevel.TRACE, "trace message a"),
            LogEntry(logger(TestServiceA::class), LogLevel.DEBUG, "debug message a"),
            LogEntry(logger(TestServiceA::class), LogLevel.INFO, "info message a"),
            LogEntry(logger(TestServiceA::class), LogLevel.WARN, "warn message a"),
            LogEntry(logger(TestServiceA::class), LogLevel.ERROR, "error message a")
        )

        customLogger.finer("trace message c")
        customLogger.fine("debug message c")
        customLogger.info("info message c")
        customLogger.warning("warn message c")
        customLogger.severe("error message c")

        logRecord.entries.shouldContainExactly(
            LogEntry(logger(TestServiceA::class), LogLevel.TRACE, "trace message a"),
            LogEntry(logger(TestServiceA::class), LogLevel.DEBUG, "debug message a"),
            LogEntry(logger(TestServiceA::class), LogLevel.INFO, "info message a"),
            LogEntry(logger(TestServiceA::class), LogLevel.WARN, "warn message a"),
            LogEntry(logger(TestServiceA::class), LogLevel.ERROR, "error message a")
        )
    }

    test("log messages are recorded for String logger").config(extensions = listOf(recordLogs("custom-logger"))) {
        customLogger.finer("trace message c")
        customLogger.fine("debug message c")
        customLogger.info("info message c")
        customLogger.warning("warn message c")
        customLogger.severe("error message c")

        logRecord.entries.shouldContainExactly(
            LogEntry("custom-logger", LogLevel.TRACE, "trace message c"),
            LogEntry("custom-logger", LogLevel.DEBUG, "debug message c"),
            LogEntry("custom-logger", LogLevel.INFO, "info message c"),
            LogEntry("custom-logger", LogLevel.WARN, "warn message c"),
            LogEntry("custom-logger", LogLevel.ERROR, "error message c")
        )
    }

    test("Throwables are recorded for logger").config(extensions = listOf(recordLogs(TestServiceA::class))) {
        val throwable = RuntimeException("error")
        testServiceA.logError(throwable)

        logRecord.entries.shouldContainExactly(
            LogEntry(
                logger = logger(TestServiceA::class),
                level = LogLevel.ERROR,
                message = "error message a",
                throwable = throwable
            )
        )
    }
})

