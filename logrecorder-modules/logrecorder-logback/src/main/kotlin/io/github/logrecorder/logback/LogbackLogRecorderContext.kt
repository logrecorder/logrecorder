package io.github.logrecorder.logback

import ch.qos.logback.classic.Logger
import io.github.logrecorder.common.LogRecorder
import io.github.logrecorder.common.LogRecorderContext

/**
 * [LogRecorderContext] for Logback.
 *
 * Allows for the [starting][start] and [stopping][stop] of the recording of multiple [Logger].
 */
internal class LogbackLogRecorderContext(loggers: Collection<Logger>) : LogRecorderContext {

    override val record = LogbackLogRecord()
    private val recorders = loggers.map { LogbackLogRecorder(it, record) }

    override fun start() {
        recorders.forEach(LogbackLogRecorder::start)
    }

    override fun stop() {
        recorders.forEach(LogbackLogRecorder::stop)
    }
}
