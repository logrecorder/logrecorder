package de.novatec.logrecorder.api

data class TestLogRecord(override val entries: List<LogEntry>) : LogRecord