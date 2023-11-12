package io.github.logrecorder.common

import io.github.logrecorder.api.LogRecord

interface LogRecorderContext {
    val record: LogRecord
    fun start()
    fun stop()
}
