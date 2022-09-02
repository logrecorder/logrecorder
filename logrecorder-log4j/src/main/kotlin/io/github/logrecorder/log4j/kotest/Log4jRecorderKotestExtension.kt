package io.github.logrecorder.log4j.kotest

import io.github.logrecorder.common.kotest.AbstractLogRecorderKotestExtension
import io.github.logrecorder.log4j.Log4jLogRecord
import io.github.logrecorder.log4j.Log4jLogRecorder
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import kotlin.reflect.KClass

@PublishedApi
internal fun recordLogs(
    vararg names: String,
) = recordLogs(classes = emptyArray(), names = names)

@PublishedApi
internal fun recordLogs(
    vararg classes: KClass<*>,
    names: Array<out String> = emptyArray()
) = Log4jRecorderKotestExtension(classes, names)

internal class Log4jRecorderKotestExtension(
    classes: Array<out KClass<*>>,
    names: Array<out String>
) : AbstractLogRecorderKotestExtension<Logger, Log4jLogRecord>(classes, names) {
    override val loggerFromKClass = { source: KClass<*> -> LogManager.getLogger(source.java) }
    override val loggerFromName = { name: String -> LogManager.getLogger(name) }
    override fun createLogRecord() = Log4jLogRecord()
    override fun createLogRecorder(logger: Logger, logRecord: Log4jLogRecord) = Log4jLogRecorder(logger, logRecord)
}
