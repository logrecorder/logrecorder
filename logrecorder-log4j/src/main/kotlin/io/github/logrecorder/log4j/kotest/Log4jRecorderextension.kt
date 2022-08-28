package io.github.logrecorder.log4j.kotest

import io.github.logrecorder.api.LogRecord
import io.github.logrecorder.common.junit5.LogRecorder
import io.github.logrecorder.log4j.Log4jLogRecord
import io.github.logrecorder.log4j.Log4jLogRecorder
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import kotlin.reflect.KClass

inline fun withRecordLoggers(
    classes: Array<out KClass<*>>,
    names: Array<out String>,
    function: (LogRecord) -> Unit
) {
    val logRecord = createLogRecord()
    val recorders = getLoggers(classes, names)
        .map {  createLogRecorder(it, logRecord) }
        .onEach { startLogger(it) }

    try {
        return function(logRecord)
    } finally {
        recorders.forEach { stopRecorder(it) }
    }
}

inline fun withRecordLoggers(
    vararg names: String,
    execution: (LogRecord) -> Unit
) {
    withRecordLoggers(emptyArray(), names, execution)
}

inline fun withRecordLoggers(
    vararg classes: KClass<*>,
    execution: (LogRecord) -> Unit
) {
    withRecordLoggers(classes, emptyArray(), execution)
}

fun getLoggers(classes: Array<out KClass<*>>, names: Array<out String>): Set<Logger> {
    val fromClasses = classes.map { LogManager.getLogger(it.java) }
    val fromNames = names.map { LogManager.getLogger(it) }
    return (fromClasses + fromNames).toSet()
}

@PublishedApi
internal fun createLogRecord() = Log4jLogRecord()
@PublishedApi
internal fun createLogRecorder(logger: Logger, logRecord: Log4jLogRecord) = Log4jLogRecorder(logger, logRecord)
@PublishedApi
internal fun startLogger(recorder: LogRecorder) {
    recorder.start()
}
@PublishedApi
internal fun stopRecorder(recorder: LogRecorder) {
    recorder.stop()
}


