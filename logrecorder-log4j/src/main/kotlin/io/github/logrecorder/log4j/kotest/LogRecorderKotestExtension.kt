package io.github.logrecorder.log4j.kotest

import io.github.logrecorder.api.LogRecord
import io.github.logrecorder.api.MutableLogRecord
import io.github.logrecorder.common.junit5.LogRecorder
import kotlin.reflect.KClass

abstract class LogRecorderKotestExtension<L : Any, LR : MutableLogRecord<*>> {
    abstract val loggerFromKClass: (KClass<*>) -> L
    abstract val loggerFromName: (String) -> L
    abstract fun createLogRecord(): LR
    abstract fun createLogRecorder(logger: L, logRecord: LR): LogRecorder

    fun getLoggers(classes: Array<out KClass<*>>, names: Array<out String>): Set<L> {
        val fromClasses = classes.map { loggerFromKClass(it) }
        val fromNames = names.map { loggerFromName(it) }
        return (fromClasses + fromNames).toSet()
    }

    internal fun startRecorder(recorder: LogRecorder) {
        recorder.start()
    }

    internal fun stopRecorder(recorder: LogRecorder) {
        recorder.stop()
    }
}

fun <L : Any, LR : MutableLogRecord<*>> withRecordLoggers(
    classes: Array<out KClass<*>>,
    names: Array<out String>,
    extension: LogRecorderKotestExtension<L, LR>,
    block: (LogRecord) -> Unit
) {
    val logRecord = extension.createLogRecord()
    val recorders = extension.getLoggers(classes, names)
        .map { extension.createLogRecorder(it, logRecord) }
        .onEach { extension.startRecorder(it) }

    try {
        return block(logRecord)
    } finally {
        recorders.forEach { extension.stopRecorder(it) }
    }
}
