package io.github.logrecorder.common

import io.github.logrecorder.api.MutableLogRecord
import kotlin.reflect.KClass

abstract class LogRecorderExecutionBase<L : Any, LR : MutableLogRecord<*>> {
    abstract fun loggerFromKClass(kClass: KClass<*>): L
    abstract fun loggerFromName(name: String): L
    abstract fun createLogRecord(): LR
    abstract fun createLogRecorder(logger: L, logRecord: LR): LogRecorder

    fun getLoggers(classes: Array<out KClass<*>>, names: Array<out String>): Set<L> {
        val fromClasses = classes.map { loggerFromKClass(it) }
        val fromNames = names.map { loggerFromName(it) }
        return (fromClasses + fromNames).toSet()
    }
}


