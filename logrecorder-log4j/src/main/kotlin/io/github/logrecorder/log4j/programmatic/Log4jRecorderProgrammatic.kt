package io.github.logrecorder.log4j.programmatic

import io.github.logrecorder.api.LogRecord
import io.github.logrecorder.common.LogRecorderExecutionBase
import io.github.logrecorder.log4j.Log4jLogRecord
import io.github.logrecorder.log4j.Log4jLogRecorder
import io.github.logrecorder.programmatic.withRecordLoggers
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import kotlin.reflect.KClass

/**
 * This support function will wrap the execution block and record the loggers specified by the parameters
 * classes and/or names and provide the [LogRecord] to the execution block.
 *
 * Recording a logger will set its log level to [org.apache.logging.log4j.Level.ALL] for the duration of the test.
 * After the test was executed, the log level will be restored to whatever it was before.
 *
 * @param classes [KClass] whose name should be used to identify loggers to record.
 * @param names String names of the [Logger] instances to record
 * @see withRecordLoggers
 * @see LogRecord
 * @since 2.4
 */
fun <T : Any> recordLoggers(
    vararg classes: KClass<*>,
    names: Array<out String> = emptyArray(),
    block: (LogRecord) -> T
) = withRecordLoggers(classes, names, Log4jRecorderProgrammatic(), block)

/**
 * @see recordLoggers
 * @since 2.4
 */
fun <T : Any> recordLoggers(
    vararg names: String,
    block: (LogRecord) -> T
) = recordLoggers(classes = emptyArray(), names = names, block)

internal class Log4jRecorderProgrammatic : LogRecorderExecutionBase<Logger, Log4jLogRecord>() {
    override val loggerFromKClass = { source: KClass<*> -> LogManager.getLogger(source.java) }
    override val loggerFromName = { name: String -> LogManager.getLogger(name) }
    override fun createLogRecord() = Log4jLogRecord()
    override fun createLogRecorder(logger: Logger, logRecord: Log4jLogRecord) = Log4jLogRecorder(logger, logRecord)
}
