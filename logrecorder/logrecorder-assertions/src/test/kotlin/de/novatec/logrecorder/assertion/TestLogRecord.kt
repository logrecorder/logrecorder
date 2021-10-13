package de.novatec.logrecorder.assertion

import de.novatec.logrecorder.api.LogEntry
import de.novatec.logrecorder.api.LogRecord

data class TestLogRecord(override val entries: List<LogEntry>) : LogRecord