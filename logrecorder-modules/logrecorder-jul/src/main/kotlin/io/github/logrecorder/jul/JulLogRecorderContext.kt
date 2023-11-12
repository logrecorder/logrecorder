package io.github.logrecorder.jul

import io.github.logrecorder.common.LogRecorderContext
import java.util.logging.Logger

class JulLogRecorderContext(loggers: Collection<Logger>) : LogRecorderContext {

    override val record = JulLogRecord()
    private val recorders = loggers.map { JulLogRecorder(it, record) }

    override fun start() {
        recorders.forEach(JulLogRecorder::start)
    }

    override fun stop() {
        recorders.forEach(JulLogRecorder::stop)
    }
}
