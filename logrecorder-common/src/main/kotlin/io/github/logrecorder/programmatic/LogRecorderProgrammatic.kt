package io.github.logrecorder.programmatic

import io.github.logrecorder.api.LogRecord
import io.github.logrecorder.api.MutableLogRecord
import io.github.logrecorder.common.LogRecorderExecutionBase
import kotlin.reflect.KClass

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
