/*
 * Copyright 2017-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.logrecorder.jul

import io.github.logrecorder.api.LogEntry
import io.github.logrecorder.api.LogLevel
import java.util.logging.Level
import java.util.logging.LogRecord

/**
 * [LogRecord][io.github.logrecorder.api.LogRecord] for JUL.
 *
 * Allows for the [recording][record] of a [LogRecord] as a [LogEntry].
 */
internal class JulLogRecord : io.github.logrecorder.api.LogRecord {

    private val recordedLogEntries: MutableList<LogEntry> = mutableListOf()

    override val entries: List<LogEntry>
        get() = ArrayList(recordedLogEntries)

    fun record(value: LogRecord) {
        val logEntry = LogEntry(
            logger = value.loggerName,
            level = when (value.level) {
                Level.FINER -> LogLevel.TRACE
                Level.FINE -> LogLevel.DEBUG
                Level.INFO -> LogLevel.INFO
                Level.WARNING -> LogLevel.WARN
                Level.SEVERE -> LogLevel.ERROR
                else -> LogLevel.UNKNOWN
            },
            message = value.message,
            throwable = value.thrown
        )
        recordedLogEntries.add(logEntry)
    }

}
