package info.novatec.testit.logrecorder.assertion

import info.novatec.testit.logrecorder.api.LogEntry
import info.novatec.testit.logrecorder.api.LogRecord

data class TestLogRecord(override val entries: List<LogEntry>) : LogRecord
