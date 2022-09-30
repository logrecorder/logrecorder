package io.github.logrecorder.jul.kotest

import io.github.logrecorder.api.LogRecord
import io.github.logrecorder.common.kotest.AbstractLogRecorderKotestExtension
import io.github.logrecorder.jul.JulLogRecord
import io.github.logrecorder.jul.JulLogRecorder
import java.util.logging.LogManager
import java.util.logging.Logger
import kotlin.reflect.KClass

/**
 * Convenience Method to instantiate [JulRecorderKotestExtension]
 * This extension will record the loggers specified by the parameters save the [LogRecord] into the CoroutineContext
 * for retrieval by logRecord
 *
 * Recording a logger will set its log level to [java.util.logging.Level.FINER] for the duration of the test.
 * After the test was executed, the log level will be restored to whatever it was before.
 *
 * @param classes [KClass] whose name should be used to identify loggers to record.
 * @param names String names of the [Logger] instances to record
 * @see JulRecorderKotestExtension
 * @since 2.4
 */
fun recordLogs(
    vararg classes: KClass<*>,
    names: Array<out String> = emptyArray()
) = JulRecorderKotestExtension(classes, names)

/**
 * @see recordLogs
 * @since 2.4
 */
fun recordLogs(
    vararg names: String,
) = recordLogs(classes = emptyArray(), names = names)

class JulRecorderKotestExtension(
    classes: Array<out KClass<*>>,
    names: Array<out String>
) : AbstractLogRecorderKotestExtension<Logger, JulLogRecord>(classes, names) {
    override val loggerFromKClass = { source: KClass<*> -> LogManager.getLogManager().getLogger(source.java.name) }
    override val loggerFromName = { name: String -> LogManager.getLogManager().getLogger(name) }
    override fun createLogRecord() = JulLogRecord()
    override fun createLogRecorder(logger: Logger, logRecord: JulLogRecord) = JulLogRecorder(logger, logRecord)
}
