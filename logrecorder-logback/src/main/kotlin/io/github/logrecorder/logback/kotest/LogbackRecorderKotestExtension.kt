package io.github.logrecorder.logback.kotest

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Logger
import io.github.logrecorder.api.LogRecord
import io.github.logrecorder.common.kotest.AbstractLogRecorderKotestExtension
import io.github.logrecorder.logback.LogbackLogRecord
import io.github.logrecorder.logback.LogbackLogRecorder
import org.slf4j.LoggerFactory
import kotlin.reflect.KClass

/**
 * Convenience Method to instantiate [LogbackRecorderKotestExtension]
 * This extension will record the loggers specified by the parameters save the [LogRecord] into the CoroutineContext
 * for retrieval by logRecord
 *
 * Recording a logger will set its log level to [Level.ALL] for the duration of the test.
 * After the test was executed, the log level will be restored to whatever it was before.
 *
 * @param classes [KClass] whose name should be used to identify loggers to record.
 * @param names String names of the [Logger] instances to record
 * @see LogbackRecorderKotestExtension
 * @since 2.4
 */
@PublishedApi
internal fun recordLogs(
    vararg classes: KClass<*>,
    names: Array<out String> = emptyArray()
) = LogbackRecorderKotestExtension(classes, names)

/**
 * @see recordLogs
 * @since 2.4
 */
@PublishedApi
internal fun recordLogs(
    vararg names: String,
) = recordLogs(classes = emptyArray(), names = names)

internal class LogbackRecorderKotestExtension(
    classes: Array<out KClass<*>>,
    names: Array<out String>
) : AbstractLogRecorderKotestExtension<Logger, LogbackLogRecord>(classes, names) {
    override val loggerFromKClass = { source: KClass<*> -> LoggerFactory.getLogger(source.java) as Logger }
    override val loggerFromName = { name: String -> LoggerFactory.getLogger(name) as Logger }
    override fun createLogRecord() = LogbackLogRecord()
    override fun createLogRecorder(logger: Logger, logRecord: LogbackLogRecord) = LogbackLogRecorder(logger, logRecord)
}
