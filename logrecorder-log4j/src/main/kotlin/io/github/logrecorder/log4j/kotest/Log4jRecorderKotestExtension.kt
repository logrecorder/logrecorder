package io.github.logrecorder.log4j.kotest

import io.github.logrecorder.api.LogRecord
import io.github.logrecorder.log4j.Log4jLogRecord
import io.github.logrecorder.log4j.Log4jLogRecorder
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import kotlin.reflect.KClass

fun withRecordLoggers(
    classes: Array<out KClass<*>> = emptyArray(),
    names: Array<out String> = emptyArray(),
    block: (LogRecord) -> Unit
) = withRecordLoggers(classes, names, getExtension(), block)

fun withRecordLoggers(
    vararg names: String,
    block: (LogRecord) -> Unit
) = withRecordLoggers(names = names, block = block)

fun withRecordLoggers(
    vararg classes: KClass<*>,
    block: (LogRecord) -> Unit
) = withRecordLoggers(classes = classes, block = block)

@PublishedApi
internal fun getExtension() = Log4jRecorderKotestExtension()

internal class Log4jRecorderKotestExtension : LogRecorderKotestExtension<Logger, Log4jLogRecord>() {

    override val loggerFromKClass = { source : KClass<*> -> LogManager.getLogger(source.java) }
    override val loggerFromName = { name : String -> LogManager.getLogger(name) }
    override fun createLogRecord() = Log4jLogRecord()
    override fun createLogRecorder(logger: Logger, logRecord: Log4jLogRecord) = Log4jLogRecorder(logger, logRecord)
}
