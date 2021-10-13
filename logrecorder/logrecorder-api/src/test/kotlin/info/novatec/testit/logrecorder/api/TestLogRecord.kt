package info.novatec.testit.logrecorder.api

data class TestLogRecord(override val entries: List<LogEntry>) : LogRecord
