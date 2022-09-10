package io.github.logrecorder.programmatic

import io.github.logrecorder.api.LogRecord
import io.github.logrecorder.api.MutableLogRecord
import io.github.logrecorder.common.LogRecorderExecutionBase
import kotlin.reflect.KClass

/**
 * With record loggers is the base function for programmatic wrapping of record logging
 *
 * @param T Return type of the execution block
 * @param L Type of the Logger (specific to logging implementation) to record
 * @param LR Type of the mutable [LogRecord] as recording of the logging
 * @param classes [KClass] whose name should be used to identify loggers to record.
 * @param names String names of the Logger (specific to logging implementation) instances to record
 * @param executionBase Logger implementation of [LogRecorderExecutionBase]
 * @param block code block to execute
 * @return optional return of the executed code block
 */
inline fun <T : Any, L : Any, LR : MutableLogRecord<*>> withRecordLoggers(
    classes: Array<out KClass<*>>,
    names: Array<out String>,
    executionBase: LogRecorderExecutionBase<L, LR>,
    block: (LogRecord) -> T
): T {
    val logRecord = executionBase.createLogRecord()
    val recorders = executionBase.getLoggers(classes, names)
        .map { executionBase.createLogRecorder(it, logRecord) }
        .onEach { it.start() }

    try {
        return block(logRecord)
    } finally {
        recorders.forEach { it.stop() }
    }
}
