package io.github.logrecorder.jul

import io.github.logrecorder.common.LogRecorderContext
import java.util.logging.Logger

/**
 * [LogRecorderContext] for Jul.
 *
 * Allows for the [starting][start] and [stopping][stop] of the recording of multiple [Logger].
 */
internal class JulLogRecorderContext(loggers: Collection<Logger>) : LogRecorderContext {

    override val record = JulLogRecord()
    private val recorders = loggers.map { JulLogRecorder(it, record) }

    override fun start() {
        recorders.forEach(JulLogRecorder::start)
    }

    override fun stop() {
        recorders.forEach(JulLogRecorder::stop)
    }
}
