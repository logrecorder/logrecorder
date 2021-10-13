package de.novatec.logrecorder.logback

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.spi.ILoggingEvent
import de.novatec.logrecorder.api.LogEntry
import de.novatec.logrecorder.api.LogLevel
import de.novatec.logrecorder.api.LogRecord

internal class LogbackLogRecord : LogRecord {

    private val recordedLogEntries: MutableList<LogEntry> = mutableListOf()

    override val entries: List<LogEntry>
        get() = ArrayList(recordedLogEntries)

    fun record(value: ILoggingEvent) {
        val logEntry = LogEntry(
            logger = value.loggerName,
            level = when (value.level) {
                Level.TRACE -> LogLevel.TRACE
                Level.DEBUG -> LogLevel.DEBUG
                Level.INFO -> LogLevel.INFO
                Level.WARN -> LogLevel.WARN
                Level.ERROR -> LogLevel.ERROR
                else -> LogLevel.UNKNOWN
            },
            message = value.formattedMessage
        )
        recordedLogEntries.add(logEntry)
    }

}
