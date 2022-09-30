package io.github.logrecorder.jul.programmatic

import io.github.logrecorder.api.LogRecord
import io.github.logrecorder.common.LogRecorderExecutionBase
import io.github.logrecorder.jul.JulLogRecord
import io.github.logrecorder.jul.JulLogRecorder
import io.github.logrecorder.programmatic.withRecordLoggers
import java.util.logging.LogManager
import java.util.logging.Logger
import kotlin.reflect.KClass

/**
 * This support function will wrap the execution block and record the loggers specified by the parameters
 * classes and/or names and provide the [LogRecord] to the execution block.
 *
 * Recording a logger will set its log level to  [java.util.logging.Level.FINER] for the duration of the test.
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
) = withRecordLoggers(classes, names, JulRecorderProgrammatic(), block)

/**
 * @see recordLoggers
 * @since 2.4
 */
fun <T : Any> recordLoggers(
    vararg names: String,
    block: (LogRecord) -> T
) = recordLoggers(classes = emptyArray(), names = names, block)

internal class JulRecorderProgrammatic : LogRecorderExecutionBase<Logger, JulLogRecord>() {
    override val loggerFromKClass = { source: KClass<*> -> LogManager.getLogManager().getLogger(source.java.name) }
    override val loggerFromName = { name: String -> LogManager.getLogManager().getLogger(name) }
    override fun createLogRecord() = JulLogRecord()
    override fun createLogRecorder(logger: Logger, logRecord: JulLogRecord) = JulLogRecorder(logger, logRecord)
}
