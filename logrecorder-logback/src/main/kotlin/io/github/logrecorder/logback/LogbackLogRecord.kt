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
package io.github.logrecorder.logback

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.spi.ILoggingEvent
import io.github.logrecorder.api.LogEntry
import io.github.logrecorder.api.LogLevel
import io.github.logrecorder.api.LogRecord

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
            message = value.formattedMessage,
            marker = value.marker?.toString()
        )
        recordedLogEntries.add(logEntry)
    }

}
