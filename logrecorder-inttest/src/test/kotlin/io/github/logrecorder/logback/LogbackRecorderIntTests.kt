package io.github.logrecorder.logback

import io.github.logrecorder.TestServiceLogback
import io.github.logrecorder.api.LogEntry
import io.github.logrecorder.api.LogLevel
import io.github.logrecorder.api.LogRecord
import io.github.logrecorder.api.LogRecord.Companion.logger
import io.github.logrecorder.common.kotest.logRecord
import io.github.logrecorder.logback.junit5.RecordLoggers
import io.github.logrecorder.logback.kotest.recordLogs
import io.github.logrecorder.logback.programmatic.recordLoggers
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactly
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class LogbackRecorderJunit5IT {

    private val testServiceLogback = TestServiceLogback()

    @Test
    @RecordLoggers(TestServiceLogback::class)
    fun `extension is registered and log messages are recorded`(log: LogRecord) {
        assertThat(log.entries).isEmpty()

        testServiceLogback.logSomething()
        assertThat(log.entries).containsExactly(
            LogEntry(logger(TestServiceLogback::class), LogLevel.TRACE, "trace message a"),
            LogEntry(logger(TestServiceLogback::class), LogLevel.DEBUG, "debug message a"),
            LogEntry(logger(TestServiceLogback::class), LogLevel.INFO, "info message a"),
            LogEntry(logger(TestServiceLogback::class), LogLevel.WARN, "warn message a"),
            LogEntry(logger(TestServiceLogback::class), LogLevel.ERROR, "error message a")
        )
    }

    @Test
    fun `works programmatic`() {
        recordLoggers(TestServiceLogback::class) { log ->
            log.entries.shouldBeEmpty()

            testServiceLogback.logSomething()
            log.entries.shouldContainExactly(
                LogEntry(logger(TestServiceLogback::class), LogLevel.TRACE, "trace message a"),
                LogEntry(logger(TestServiceLogback::class), LogLevel.DEBUG, "debug message a"),
                LogEntry(logger(TestServiceLogback::class), LogLevel.INFO, "info message a"),
                LogEntry(logger(TestServiceLogback::class), LogLevel.WARN, "warn message a"),
                LogEntry(logger(TestServiceLogback::class), LogLevel.ERROR, "error message a")
            )
        }
    }
}

class LogbackRecorderKotestIT : FunSpec({
    val testServiceLogback = TestServiceLogback()

    test("works as extension").config(
        extensions = listOf(recordLogs(TestServiceLogback::class))
    ) {
        logRecord.entries.shouldBeEmpty()

        testServiceLogback.logSomething()
        logRecord.entries.shouldContainExactly(
            LogEntry(logger(TestServiceLogback::class), LogLevel.TRACE, "trace message a"),
            LogEntry(logger(TestServiceLogback::class), LogLevel.DEBUG, "debug message a"),
            LogEntry(logger(TestServiceLogback::class), LogLevel.INFO, "info message a"),
            LogEntry(logger(TestServiceLogback::class), LogLevel.WARN, "warn message a"),
            LogEntry(logger(TestServiceLogback::class), LogLevel.ERROR, "error message a")
        )


    }

    test("works programmatic") {
        recordLoggers(TestServiceLogback::class) { log ->
            log.entries.shouldBeEmpty()

            testServiceLogback.logSomething()
            log.entries.shouldContainExactly(
                LogEntry(logger(TestServiceLogback::class), LogLevel.TRACE, "trace message a"),
                LogEntry(logger(TestServiceLogback::class), LogLevel.DEBUG, "debug message a"),
                LogEntry(logger(TestServiceLogback::class), LogLevel.INFO, "info message a"),
                LogEntry(logger(TestServiceLogback::class), LogLevel.WARN, "warn message a"),
                LogEntry(logger(TestServiceLogback::class), LogLevel.ERROR, "error message a")
            )
        }
    }
})

