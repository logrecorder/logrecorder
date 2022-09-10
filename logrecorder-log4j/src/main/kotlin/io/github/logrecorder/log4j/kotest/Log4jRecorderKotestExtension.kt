package io.github.logrecorder.log4j.kotest

import io.github.logrecorder.api.LogRecord
import io.github.logrecorder.common.kotest.AbstractLogRecorderKotestExtension
import io.github.logrecorder.log4j.Log4jLogRecord
import io.github.logrecorder.log4j.Log4jLogRecorder
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import kotlin.reflect.KClass

/**
 * Convenience Method to instantiate [Log4jRecorderKotestExtension]
 * This extension will record the loggers specified by the parameters save the [LogRecord] into the CoroutineContext
 * for retrieval by logRecord
 *
 * Recording a logger will set its log level to [org.apache.logging.log4j.Level.ALL] for the duration of the test.
 * After the test was executed, the log level will be restored to whatever it was before.
 *
 * @param classes [KClass] whose name should be used to identify loggers to record.
 * @param names String names of the [Logger] instances to record
 * @see Log4jRecorderKotestExtension
 * @since 2.4
 */
@PublishedApi
internal fun recordLogs(
    vararg classes: KClass<*>,
    names: Array<out String> = emptyArray()
) = Log4jRecorderKotestExtension(classes, names)

/**
 * @see recordLogs
 * @since 2.4
 */
@PublishedApi
internal fun recordLogs(
    vararg names: String,
) = recordLogs(classes = emptyArray(), names = names)

internal class Log4jRecorderKotestExtension(
    classes: Array<out KClass<*>>,
    names: Array<out String>
) : AbstractLogRecorderKotestExtension<Logger, Log4jLogRecord>(classes, names) {
    override val loggerFromKClass = { source: KClass<*> -> LogManager.getLogger(source.java) }
    override val loggerFromName = { name: String -> LogManager.getLogger(name) }
    override fun createLogRecord() = Log4jLogRecord()
    override fun createLogRecorder(logger: Logger, logRecord: Log4jLogRecord) = Log4jLogRecorder(logger, logRecord)
}
