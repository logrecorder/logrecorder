package io.github.logrecorder.log4j.programmatic

import io.github.logrecorder.api.LogEntry
import io.github.logrecorder.api.LogLevel.*
import io.github.logrecorder.api.LogRecord.Companion.logger
import io.github.logrecorder.log4j.junit5.TestServiceA
import io.github.logrecorder.log4j.junit5.TestServiceB
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactly
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.ThreadContext

class Log4jRecorderProgrammaticTests : FunSpec({

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

    test("log messages are recorded") {
        withRecordLoggers(TestServiceA::class, TestServiceB::class, names = arrayOf("custom-logger")) { log ->
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

            customLogger.trace("trace message c")
            customLogger.debug("debug message c")
            customLogger.info("info message c")
            customLogger.warn("warn message c")
            customLogger.error("error message c")

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
        withRecordLoggers(TestServiceA::class) { log ->
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

            customLogger.trace("trace message c")
            customLogger.debug("debug message c")
            customLogger.info("info message c")
            customLogger.warn("warn message c")
            customLogger.error("error message c")

            log.entries.shouldContainExactly(
                LogEntry(logger(TestServiceA::class), TRACE, "trace message a"),
                LogEntry(logger(TestServiceA::class), DEBUG, "debug message a"),
                LogEntry(logger(TestServiceA::class), INFO, "info message a"),
                LogEntry(logger(TestServiceA::class), WARN, "warn message a"),
                LogEntry(logger(TestServiceA::class), ERROR, "error message a")
            )
        }
    }

    test("MDC properties are recorded") {
        withRecordLoggers(TestServiceA::class) { log ->
            ThreadContext.put("custom#1", "foo")
            ThreadContext.put("custom#2", "bar")
            testServiceA.logSingleInfo()
            ThreadContext.remove("custom#2")
            testServiceA.logSingleInfo()

            log.entries.shouldContainExactly(
                LogEntry(
                    logger = logger(TestServiceA::class),
                    level = INFO,
                    message = "info message a",
                    properties = mapOf(
                        "custom#1" to "foo",
                        "custom#2" to "bar"
                    )
                ),
                LogEntry(
                    logger = logger(TestServiceA::class),
                    level = INFO,
                    message = "info message a",
                    properties = mapOf(
                        "custom#1" to "foo"
                    )
                )
            )
        }
    }

    test("log messages are recorded for only String logger") {
        withRecordLoggers("custom-logger") { log ->
            customLogger.trace("trace message c")
            customLogger.debug("debug message c")
            customLogger.info("info message c")
            customLogger.warn("warn message c")
            customLogger.error("error message c")

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


