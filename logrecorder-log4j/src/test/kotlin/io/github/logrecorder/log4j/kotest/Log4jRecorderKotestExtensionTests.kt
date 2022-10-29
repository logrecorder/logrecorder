package io.github.logrecorder.log4j.kotest

import io.github.logrecorder.api.LogEntry
import io.github.logrecorder.api.LogLevel
import io.github.logrecorder.api.LogRecord.Companion.logger
import io.github.logrecorder.common.kotest.logRecord
import io.github.logrecorder.log4j.util.TestServiceA
import io.github.logrecorder.log4j.util.TestServiceB
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactly
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.ThreadContext

class Log4jRecorderKotestExtensionTests : FunSpec({
    val customLogger = LogManager.getLogger("custom-logger")

    val testServiceA = TestServiceA()
    val testServiceB = TestServiceB()

    beforeEach {
        testServiceA.logSomething()
        testServiceB.logSomething()
    }

    afterEach {
        ThreadContext.clearAll()
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

        customLogger.trace("trace message c")
        customLogger.debug("debug message c")
        customLogger.info("info message c")
        customLogger.warn("warn message c")
        customLogger.error("error message c")

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

        customLogger.trace("trace message c")
        customLogger.debug("debug message c")
        customLogger.info("info message c")
        customLogger.warn("warn message c")
        customLogger.error("error message c")

        logRecord.entries.shouldContainExactly(
            LogEntry(logger(TestServiceA::class), LogLevel.TRACE, "trace message a"),
            LogEntry(logger(TestServiceA::class), LogLevel.DEBUG, "debug message a"),
            LogEntry(logger(TestServiceA::class), LogLevel.INFO, "info message a"),
            LogEntry(logger(TestServiceA::class), LogLevel.WARN, "warn message a"),
            LogEntry(logger(TestServiceA::class), LogLevel.ERROR, "error message a")
        )
    }

    test("MDC properties are recorded").config(extensions = listOf(recordLogs(TestServiceA::class))) {
        ThreadContext.put("custom#1", "foo")
        ThreadContext.put("custom#2", "bar")
        testServiceA.logSingleInfo()
        ThreadContext.remove("custom#2")
        testServiceA.logSingleInfo()

        logRecord.entries.shouldContainExactly(
            LogEntry(
                logger = logger(TestServiceA::class),
                level = LogLevel.INFO,
                message = "info message a",
                properties = mapOf(
                    "custom#1" to "foo",
                    "custom#2" to "bar"
                )
            ),
            LogEntry(
                logger = logger(TestServiceA::class),
                level = LogLevel.INFO,
                message = "info message a",
                properties = mapOf(
                    "custom#1" to "foo"
                )
            )
        )
    }

    test("log messages are recorded for String logger").config(extensions = listOf(recordLogs("custom-logger"))) {
        customLogger.trace("trace message c")
        customLogger.debug("debug message c")
        customLogger.info("info message c")
        customLogger.warn("warn message c")
        customLogger.error("error message c")

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

