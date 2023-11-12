package io.github.logrecorder.common

import io.github.logrecorder.api.LogRecord

/**
 * Internal interface implemented by log-framework modules to allow the starting and stopping of a recording.
 *
 * Not intended to be implemented outside the core modules.
 * We might change this interface at any time.
 */
interface LogRecorderContext {
    val record: LogRecord
    fun start()
    fun stop()
}
