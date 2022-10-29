package io.github.logrecorder.jul

import io.github.logrecorder.TestServiceJul
import io.github.logrecorder.api.LogEntry
import io.github.logrecorder.api.LogLevel
import io.github.logrecorder.api.LogRecord
import io.github.logrecorder.api.LogRecord.Companion.logger
import io.github.logrecorder.common.kotest.logRecord
import io.github.logrecorder.jul.junit5.RecordLoggers
import io.github.logrecorder.jul.kotest.recordLogs
import io.github.logrecorder.jul.programmatic.recordLoggers
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactly
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class JulRecorderJunit5IT {

    private val testServiceJul = TestServiceJul()

    @Test
    @RecordLoggers(TestServiceJul::class)
    fun `extension is registered and log messages are recorded`(log: LogRecord) {
        assertThat(log.entries).isEmpty()

        testServiceJul.logSomething()
        assertThat(log.entries).containsExactly(
            LogEntry(logger(TestServiceJul::class), LogLevel.TRACE, "trace message a"),
            LogEntry(logger(TestServiceJul::class), LogLevel.DEBUG, "debug message a"),
            LogEntry(logger(TestServiceJul::class), LogLevel.INFO, "info message a"),
            LogEntry(logger(TestServiceJul::class), LogLevel.WARN, "warn message a"),
            LogEntry(logger(TestServiceJul::class), LogLevel.ERROR, "error message a")
        )
    }

    @Test
    fun `works programmatic`() {
        recordLoggers(TestServiceJul::class) { log ->
            log.entries.shouldBeEmpty()

            testServiceJul.logSomething()
            log.entries.shouldContainExactly(
                LogEntry(logger(TestServiceJul::class), LogLevel.TRACE, "trace message a"),
                LogEntry(logger(TestServiceJul::class), LogLevel.DEBUG, "debug message a"),
                LogEntry(logger(TestServiceJul::class), LogLevel.INFO, "info message a"),
                LogEntry(logger(TestServiceJul::class), LogLevel.WARN, "warn message a"),
                LogEntry(logger(TestServiceJul::class), LogLevel.ERROR, "error message a")
            )
        }
    }
}

class JulRecorderKotestIT : FunSpec({
    val testServiceJul = TestServiceJul()

    test("works as extension").config(
        extensions = listOf(recordLogs(TestServiceJul::class))
    ) {
        logRecord.entries.shouldBeEmpty()

        testServiceJul.logSomething()
        logRecord.entries.shouldContainExactly(
            LogEntry(logger(TestServiceJul::class), LogLevel.TRACE, "trace message a"),
            LogEntry(logger(TestServiceJul::class), LogLevel.DEBUG, "debug message a"),
            LogEntry(logger(TestServiceJul::class), LogLevel.INFO, "info message a"),
            LogEntry(logger(TestServiceJul::class), LogLevel.WARN, "warn message a"),
            LogEntry(logger(TestServiceJul::class), LogLevel.ERROR, "error message a")
        )


    }

    test("works programmatic") {
        recordLoggers(TestServiceJul::class) { log ->
            log.entries.shouldBeEmpty()

            testServiceJul.logSomething()
            log.entries.shouldContainExactly(
                LogEntry(logger(TestServiceJul::class), LogLevel.TRACE, "trace message a"),
                LogEntry(logger(TestServiceJul::class), LogLevel.DEBUG, "debug message a"),
                LogEntry(logger(TestServiceJul::class), LogLevel.INFO, "info message a"),
                LogEntry(logger(TestServiceJul::class), LogLevel.WARN, "warn message a"),
                LogEntry(logger(TestServiceJul::class), LogLevel.ERROR, "error message a")
            )
        }
    }
})

