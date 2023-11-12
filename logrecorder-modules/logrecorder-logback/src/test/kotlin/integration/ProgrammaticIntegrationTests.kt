package integration

import io.github.logrecorder.api.LogEntry
import io.github.logrecorder.api.LogLevel
import io.github.logrecorder.api.LogRecord.Companion.logger
import io.github.logrecorder.logback.programmatic.recordLoggers
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactly
import org.junit.platform.commons.annotation.Testable
import org.slf4j.LoggerFactory
import org.slf4j.MDC
import org.slf4j.helpers.BasicMarkerFactory

@Testable
class ProgrammaticIntegrationTests : FunSpec({

    val customLogger = LoggerFactory.getLogger("custom-logger")

    val testServiceA = TestServiceA()
    val testServiceB = TestServiceB()

    beforeEach {
        testServiceA.logSomething()
        testServiceB.logSomething()
    }

    afterEach {
        MDC.clear()
    }

    test("log messages are recorded") {
        recordLoggers(TestServiceA::class, TestServiceB::class, names = arrayOf("custom-logger")) { log ->
            log.entries.shouldBeEmpty()

            testServiceA.logSomething()
            log.entries.shouldContainExactly(
                LogEntry(logger(TestServiceA::class), LogLevel.TRACE, "trace message a", "marker a"),
                LogEntry(logger(TestServiceA::class), LogLevel.DEBUG, "debug message a", "marker a"),
                LogEntry(logger(TestServiceA::class), LogLevel.INFO, "info message a", "marker a"),
                LogEntry(logger(TestServiceA::class), LogLevel.WARN, "warn message a", "marker a"),
                LogEntry(logger(TestServiceA::class), LogLevel.ERROR, "error message a", "marker a")
            )

            testServiceB.logSomething()
            log.entries.shouldContainExactly(
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

            log.entries.shouldContainExactly(
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
    }

    test("log messages are recorded from ServiceA") {
        recordLoggers(TestServiceA::class) { log ->
            log.entries.shouldBeEmpty()

            testServiceA.logSomething()
            log.entries.shouldContainExactly(
                LogEntry(logger(TestServiceA::class), LogLevel.TRACE, "trace message a", "marker a"),
                LogEntry(logger(TestServiceA::class), LogLevel.DEBUG, "debug message a", "marker a"),
                LogEntry(logger(TestServiceA::class), LogLevel.INFO, "info message a", "marker a"),
                LogEntry(logger(TestServiceA::class), LogLevel.WARN, "warn message a", "marker a"),
                LogEntry(logger(TestServiceA::class), LogLevel.ERROR, "error message a", "marker a")
            )

            testServiceB.logSomething()
            log.entries.shouldContainExactly(
                LogEntry(logger(TestServiceA::class), LogLevel.TRACE, "trace message a", "marker a"),
                LogEntry(logger(TestServiceA::class), LogLevel.DEBUG, "debug message a", "marker a"),
                LogEntry(logger(TestServiceA::class), LogLevel.INFO, "info message a", "marker a"),
                LogEntry(logger(TestServiceA::class), LogLevel.WARN, "warn message a", "marker a"),
                LogEntry(logger(TestServiceA::class), LogLevel.ERROR, "error message a", "marker a")
            )

            val marker = BasicMarkerFactory().getMarker("marker c")
            customLogger.trace(marker, "trace message c")
            customLogger.debug(marker, "debug message c")
            customLogger.info(marker, "info message c")
            customLogger.warn(marker, "warn message c")
            customLogger.error(marker, "error message c")

            log.entries.shouldContainExactly(
                LogEntry(logger(TestServiceA::class), LogLevel.TRACE, "trace message a", "marker a"),
                LogEntry(logger(TestServiceA::class), LogLevel.DEBUG, "debug message a", "marker a"),
                LogEntry(logger(TestServiceA::class), LogLevel.INFO, "info message a", "marker a"),
                LogEntry(logger(TestServiceA::class), LogLevel.WARN, "warn message a", "marker a"),
                LogEntry(logger(TestServiceA::class), LogLevel.ERROR, "error message a", "marker a")
            )
        }
    }

    test("MDC properties are recorded") {
        recordLoggers(TestServiceA::class) { log ->
            MDC.put("custom#1", "foo")
            MDC.put("custom#2", "bar")
            testServiceA.logSingleInfo()
            MDC.remove("custom#2")
            testServiceA.logSingleInfo()

            log.entries.shouldContainExactly(
                LogEntry(
                    logger = logger(TestServiceA::class),
                    level = LogLevel.INFO,
                    message = "info message a",
                    marker = "marker a",
                    properties = mapOf(
                        "custom#1" to "foo",
                        "custom#2" to "bar"
                    )
                ),
                LogEntry(
                    logger = logger(TestServiceA::class),
                    level = LogLevel.INFO,
                    message = "info message a",
                    marker = "marker a",
                    properties = mapOf(
                        "custom#1" to "foo"
                    )
                )
            )
        }
    }

    test("log messages are recorded for String logger") {
        recordLoggers("custom-logger") { log ->
            val marker = BasicMarkerFactory().getMarker("marker c")
            customLogger.trace(marker, "trace message c")
            customLogger.debug(marker, "debug message c")
            customLogger.info(marker, "info message c")
            customLogger.warn(marker, "warn message c")
            customLogger.error(marker, "error message c")

            log.entries.shouldContainExactly(
                LogEntry("custom-logger", LogLevel.TRACE, "trace message c", "marker c"),
                LogEntry("custom-logger", LogLevel.DEBUG, "debug message c", "marker c"),
                LogEntry("custom-logger", LogLevel.INFO, "info message c", "marker c"),
                LogEntry("custom-logger", LogLevel.WARN, "warn message c", "marker c"),
                LogEntry("custom-logger", LogLevel.ERROR, "error message c", "marker c")
            )
        }
    }

    test("Throwables are recorded for logger") {
        recordLoggers(TestServiceA::class) { log ->
            val throwable = RuntimeException("error")
            testServiceA.logError(throwable)

            log.entries.shouldContainExactly(
                LogEntry(
                    logger = logger(TestServiceA::class),
                    level = LogLevel.ERROR,
                    message = "error message a",
                    throwable = throwable
                )
            )
        }
    }
})


