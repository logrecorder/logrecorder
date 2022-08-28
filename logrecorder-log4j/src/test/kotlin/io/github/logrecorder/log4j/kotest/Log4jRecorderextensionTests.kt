package io.github.logrecorder.log4j.kotest

import io.github.logrecorder.api.LogEntry
import io.github.logrecorder.api.LogLevel
import io.github.logrecorder.api.LogRecord
import io.github.logrecorder.log4j.junit5.TestServiceA
import io.github.logrecorder.log4j.junit5.TestServiceB
import io.kotest.core.spec.style.FunSpec
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.ThreadContext
import org.assertj.core.api.Assertions

class Log4jRecorderextensionTests : FunSpec({

    val customLogger = LogManager.getLogger("custom-logger")

    val testServiceA = TestServiceA()
    val testServiceB = TestServiceB()

    beforeAny {
        testServiceA.logSomething()
        testServiceB.logSomething()
    }

    afterAny {
        ThreadContext.clearAll()
    }

    test("log messages are recorded") {
        withRecordLoggers(arrayOf(TestServiceA::class, TestServiceB::class), arrayOf("custom-logger")) { log ->
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

            customLogger.trace("trace message c")
            customLogger.debug("debug message c")
            customLogger.info("info message c")
            customLogger.warn("warn message c")
            customLogger.error("error message c")

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
    }

    test("log messages are recorded from ServiceA") {
        withRecordLoggers(TestServiceA::class) { log ->
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

            customLogger.trace("trace message c")
            customLogger.debug("debug message c")
            customLogger.info("info message c")
            customLogger.warn("warn message c")
            customLogger.error("error message c")

            Assertions.assertThat(log.entries).containsExactly(
                LogEntry(LogRecord.logger(TestServiceA::class), LogLevel.TRACE, "trace message a"),
                LogEntry(LogRecord.logger(TestServiceA::class), LogLevel.DEBUG, "debug message a"),
                LogEntry(LogRecord.logger(TestServiceA::class), LogLevel.INFO, "info message a"),
                LogEntry(LogRecord.logger(TestServiceA::class), LogLevel.WARN, "warn message a"),
                LogEntry(LogRecord.logger(TestServiceA::class), LogLevel.ERROR, "error message a")
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

            Assertions.assertThat(log.entries).containsExactly(
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

})


