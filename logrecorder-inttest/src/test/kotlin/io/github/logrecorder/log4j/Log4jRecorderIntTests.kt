package io.github.logrecorder.log4j

import io.github.logrecorder.TestServiceLog4J
import io.github.logrecorder.api.LogEntry
import io.github.logrecorder.api.LogLevel
import io.github.logrecorder.api.LogRecord
import io.github.logrecorder.api.LogRecord.Companion.logger
import io.github.logrecorder.common.kotest.logRecord
import io.github.logrecorder.log4j.junit5.RecordLoggers
import io.github.logrecorder.log4j.kotest.recordLogs
import io.github.logrecorder.log4j.programmatic.recordLoggers
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactly
import org.apache.logging.log4j.ThreadContext
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test

class Log4jRecorderJunit5IT {

    private val testServiceLog4J = TestServiceLog4J()

    @AfterEach
    fun cleanup() {
        ThreadContext.clearAll()
    }

    @Test
    @RecordLoggers(TestServiceLog4J::class)
    fun `extension is registered and log messages are recorded`(log: LogRecord) {
        assertThat(log.entries).isEmpty()

        testServiceLog4J.logSomething()
        assertThat(log.entries).containsExactly(
            LogEntry(logger(TestServiceLog4J::class), LogLevel.TRACE, "trace message a"),
            LogEntry(logger(TestServiceLog4J::class), LogLevel.DEBUG, "debug message a"),
            LogEntry(logger(TestServiceLog4J::class), LogLevel.INFO, "info message a"),
            LogEntry(logger(TestServiceLog4J::class), LogLevel.WARN, "warn message a"),
            LogEntry(logger(TestServiceLog4J::class), LogLevel.ERROR, "error message a")
        )
    }

    @Test
    fun `works programmatic`() {
        recordLoggers(TestServiceLog4J::class) { log ->
            log.entries.shouldBeEmpty()

            testServiceLog4J.logSomething()
            log.entries.shouldContainExactly(
                LogEntry(logger(TestServiceLog4J::class), LogLevel.TRACE, "trace message a"),
                LogEntry(logger(TestServiceLog4J::class), LogLevel.DEBUG, "debug message a"),
                LogEntry(logger(TestServiceLog4J::class), LogLevel.INFO, "info message a"),
                LogEntry(logger(TestServiceLog4J::class), LogLevel.WARN, "warn message a"),
                LogEntry(logger(TestServiceLog4J::class), LogLevel.ERROR, "error message a")
            )
        }
    }
}

class Log4jRecorderKotestIT : FunSpec({
    val testServiceLog4J = TestServiceLog4J()

    afterEach {
        ThreadContext.clearAll()
    }

    test("works as extension").config(
        extensions = listOf(recordLogs(TestServiceLog4J::class))
    ) {
        logRecord.entries.shouldBeEmpty()

        testServiceLog4J.logSomething()
        logRecord.entries.shouldContainExactly(
            LogEntry(logger(TestServiceLog4J::class), LogLevel.TRACE, "trace message a"),
            LogEntry(logger(TestServiceLog4J::class), LogLevel.DEBUG, "debug message a"),
            LogEntry(logger(TestServiceLog4J::class), LogLevel.INFO, "info message a"),
            LogEntry(logger(TestServiceLog4J::class), LogLevel.WARN, "warn message a"),
            LogEntry(logger(TestServiceLog4J::class), LogLevel.ERROR, "error message a")
        )


    }

    test("works programmatic") {
        recordLoggers(TestServiceLog4J::class) { log ->
            log.entries.shouldBeEmpty()

            testServiceLog4J.logSomething()
            log.entries.shouldContainExactly(
                LogEntry(logger(TestServiceLog4J::class), LogLevel.TRACE, "trace message a"),
                LogEntry(logger(TestServiceLog4J::class), LogLevel.DEBUG, "debug message a"),
                LogEntry(logger(TestServiceLog4J::class), LogLevel.INFO, "info message a"),
                LogEntry(logger(TestServiceLog4J::class), LogLevel.WARN, "warn message a"),
                LogEntry(logger(TestServiceLog4J::class), LogLevel.ERROR, "error message a")
            )
        }
    }
})

