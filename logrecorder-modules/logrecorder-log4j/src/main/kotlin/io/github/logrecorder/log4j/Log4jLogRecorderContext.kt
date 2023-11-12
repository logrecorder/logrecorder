package io.github.logrecorder.log4j

import io.github.logrecorder.common.LogRecorderContext
import org.apache.logging.log4j.Logger

class Log4jLogRecorderContext(loggers: Collection<Logger>) : LogRecorderContext {

    override val record = Log4jLogRecord()
    private val recorders = loggers.map { Log4jLogRecorder(it, record) }

    override fun start() {
        recorders.forEach(Log4jLogRecorder::start)
    }

    override fun stop() {
        recorders.forEach(Log4jLogRecorder::stop)
    }
}
