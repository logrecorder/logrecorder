package de.novatec.logrecorder.logback

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Logger
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.Appender

internal class LogbackLogRecorder(
    private val logger: Logger,
    logRecord: LogbackLogRecord
) {

    private val originalLevel = logger.effectiveLevel
    private val appender = CallbackAppender(name(), logRecord::record)

    fun start() {
        logger.level = Level.ALL
        logger.addAppender(appender)
    }

    fun stop() {
        logger.detachAppender(appender)
        logger.level = originalLevel
    }

    private fun name(): String = "temporary log appender #${System.nanoTime()}"

}
