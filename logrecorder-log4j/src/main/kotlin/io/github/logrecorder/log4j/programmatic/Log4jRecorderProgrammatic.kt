package io.github.logrecorder.log4j.programmatic

import io.github.logrecorder.api.LogRecord
import io.github.logrecorder.common.LogRecorderExecutionBase
import io.github.logrecorder.log4j.Log4jLogRecord
import io.github.logrecorder.log4j.Log4jLogRecorder
import io.github.logrecorder.programmatic.withRecordLoggers
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import kotlin.reflect.KClass

fun <T : Any> withRecordLoggers(
    vararg classes: KClass<*>,
    names: Array<out String> = emptyArray(),
    block: (LogRecord) -> T
) = withRecordLoggers(classes, names, Log4jRecorderProgrammatic(), block)

fun <T : Any> withRecordLoggers(
    vararg names: String,
    block: (LogRecord) -> T
) = withRecordLoggers(emptyArray(), names, Log4jRecorderProgrammatic(), block)

internal class Log4jRecorderProgrammatic : LogRecorderExecutionBase<Logger, Log4jLogRecord>() {
    override val loggerFromKClass = { source: KClass<*> -> LogManager.getLogger(source.java) }
    override val loggerFromName = { name: String -> LogManager.getLogger(name) }
    override fun createLogRecord() = Log4jLogRecord()
    override fun createLogRecorder(logger: Logger, logRecord: Log4jLogRecord) = Log4jLogRecorder(logger, logRecord)
}
