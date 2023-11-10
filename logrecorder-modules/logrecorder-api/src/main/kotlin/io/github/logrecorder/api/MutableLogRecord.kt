package io.github.logrecorder.api

interface MutableLogRecord<T : Any> : LogRecord {
    fun record(value: T)
}
