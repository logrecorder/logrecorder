package io.github.logrecorder.logback.programmatic

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Logger
import io.github.logrecorder.api.LogRecord
import io.github.logrecorder.common.LogRecorderExecutionBase
import io.github.logrecorder.logback.LogbackLogRecord
import io.github.logrecorder.logback.LogbackLogRecorder
import io.github.logrecorder.programmatic.withRecordLoggers
import org.slf4j.LoggerFactory
import kotlin.reflect.KClass

/**
 * This support function will wrap the execution block and record the loggers specified by the parameters
 * classes and/or names and provide the [LogRecord] to the execution block.
 *
 * Recording a logger will set its log level to  [Level.ALL] for the duration of the test.
 * After the test was executed, the log level will be restored to whatever it was before.
 *
 * @param classes [KClass] whose name should be used to identify loggers to record.
 * @param names String names of the [Logger] instances to record
 * @see withRecordLoggers
 * @see LogRecord
 * @since 2.4
 */
fun <T : Any> recordLogback(
    vararg classes: KClass<*>,
    names: Array<out String> = emptyArray(),
    block: (LogRecord) -> T
) = withRecordLoggers(classes, names, LogbackRecorderProgrammatic(), block)

/**
 * @see recordLogback
 * @since 2.4
 */
fun <T : Any> recordLogback(
    vararg names: String,
    block: (LogRecord) -> T
) = recordLogback(classes = emptyArray(), names = names, block)

internal class LogbackRecorderProgrammatic : LogRecorderExecutionBase<Logger, LogbackLogRecord>() {
    override val loggerFromKClass = { source: KClass<*> -> LoggerFactory.getLogger(source.java) as Logger }
    override val loggerFromName = { name: String -> LoggerFactory.getLogger(name) as Logger }
    override fun createLogRecord() = LogbackLogRecord()
    override fun createLogRecorder(logger: Logger, logRecord: LogbackLogRecord) = LogbackLogRecorder(logger, logRecord)
}
