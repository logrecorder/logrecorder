package io.github.logrecorder.jul.programmatic

import io.github.logrecorder.api.LogEntry
import io.github.logrecorder.api.LogLevel.*
import io.github.logrecorder.api.LogRecord.Companion.logger
import io.github.logrecorder.jul.util.TestServiceA
import io.github.logrecorder.jul.util.TestServiceB
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactly
import java.util.logging.Level
import java.util.logging.Logger

class JulRecorderProgrammaticTests : FunSpec({

    val customLogger = Logger.getLogger("custom-logger")

    val testServiceA = TestServiceA()
    val testServiceB = TestServiceB()

    beforeEach {
        testServiceA.logSomething()
        testServiceB.logSomething()
        customLogger.level = Level.WARNING
    }

    test("log messages are recorded") {
        recordJul(TestServiceA::class, TestServiceB::class, names = arrayOf("custom-logger")) { log ->
            log.entries.shouldBeEmpty()

            testServiceA.logSomething()
            log.entries.shouldContainExactly(
                LogEntry(logger(TestServiceA::class), TRACE, "trace message a"),
                LogEntry(logger(TestServiceA::class), DEBUG, "debug message a"),
                LogEntry(logger(TestServiceA::class), INFO, "info message a"),
                LogEntry(logger(TestServiceA::class), WARN, "warn message a"),
                LogEntry(logger(TestServiceA::class), ERROR, "error message a")
            )

            testServiceB.logSomething()
            log.entries.shouldContainExactly(
                LogEntry(logger(TestServiceA::class), TRACE, "trace message a"),
                LogEntry(logger(TestServiceA::class), DEBUG, "debug message a"),
                LogEntry(logger(TestServiceA::class), INFO, "info message a"),
                LogEntry(logger(TestServiceA::class), WARN, "warn message a"),
                LogEntry(logger(TestServiceA::class), ERROR, "error message a"),

                LogEntry(logger(TestServiceB::class), TRACE, "trace message b"),
                LogEntry(logger(TestServiceB::class), DEBUG, "debug message b"),
                LogEntry(logger(TestServiceB::class), INFO, "info message b"),
                LogEntry(logger(TestServiceB::class), WARN, "warn message b"),
                LogEntry(logger(TestServiceB::class), ERROR, "error message b")
            )

            customLogger.finer("trace message c")
            customLogger.fine("debug message c")
            customLogger.info("info message c")
            customLogger.warning("warn message c")
            customLogger.severe("error message c")

            log.entries.shouldContainExactly(
                LogEntry(logger(TestServiceA::class), TRACE, "trace message a"),
                LogEntry(logger(TestServiceA::class), DEBUG, "debug message a"),
                LogEntry(logger(TestServiceA::class), INFO, "info message a"),
                LogEntry(logger(TestServiceA::class), WARN, "warn message a"),
                LogEntry(logger(TestServiceA::class), ERROR, "error message a"),

                LogEntry(logger(TestServiceB::class), TRACE, "trace message b"),
                LogEntry(logger(TestServiceB::class), DEBUG, "debug message b"),
                LogEntry(logger(TestServiceB::class), INFO, "info message b"),
                LogEntry(logger(TestServiceB::class), WARN, "warn message b"),
                LogEntry(logger(TestServiceB::class), ERROR, "error message b"),

                LogEntry("custom-logger", TRACE, "trace message c"),
                LogEntry("custom-logger", DEBUG, "debug message c"),
                LogEntry("custom-logger", INFO, "info message c"),
                LogEntry("custom-logger", WARN, "warn message c"),
                LogEntry("custom-logger", ERROR, "error message c")
            )
        }
    }

    test("log messages are recorded from ServiceA") {
        recordJul(TestServiceA::class) { log ->
            log.entries.shouldBeEmpty()

            testServiceA.logSomething()
            log.entries.shouldContainExactly(
                LogEntry(logger(TestServiceA::class), TRACE, "trace message a"),
                LogEntry(logger(TestServiceA::class), DEBUG, "debug message a"),
                LogEntry(logger(TestServiceA::class), INFO, "info message a"),
                LogEntry(logger(TestServiceA::class), WARN, "warn message a"),
                LogEntry(logger(TestServiceA::class), ERROR, "error message a")
            )

            testServiceB.logSomething()
            log.entries.shouldContainExactly(
                LogEntry(logger(TestServiceA::class), TRACE, "trace message a"),
                LogEntry(logger(TestServiceA::class), DEBUG, "debug message a"),
                LogEntry(logger(TestServiceA::class), INFO, "info message a"),
                LogEntry(logger(TestServiceA::class), WARN, "warn message a"),
                LogEntry(logger(TestServiceA::class), ERROR, "error message a")
            )

            customLogger.finer("trace message c")
            customLogger.fine("debug message c")
            customLogger.info("info message c")
            customLogger.warning("warn message c")
            customLogger.severe("error message c")

            log.entries.shouldContainExactly(
                LogEntry(logger(TestServiceA::class), TRACE, "trace message a"),
                LogEntry(logger(TestServiceA::class), DEBUG, "debug message a"),
                LogEntry(logger(TestServiceA::class), INFO, "info message a"),
                LogEntry(logger(TestServiceA::class), WARN, "warn message a"),
                LogEntry(logger(TestServiceA::class), ERROR, "error message a")
            )
        }
    }

    test("log messages are recorded for String logger") {
        recordJul("custom-logger") { log ->
            customLogger.finer("trace message c")
            customLogger.fine("debug message c")
            customLogger.info("info message c")
            customLogger.warning("warn message c")
            customLogger.severe("error message c")

            log.entries.shouldContainExactly(
                LogEntry("custom-logger", TRACE, "trace message c"),
                LogEntry("custom-logger", DEBUG, "debug message c"),
                LogEntry("custom-logger", INFO, "info message c"),
                LogEntry("custom-logger", WARN, "warn message c"),
                LogEntry("custom-logger", ERROR, "error message c")
            )
        }
    }
})


