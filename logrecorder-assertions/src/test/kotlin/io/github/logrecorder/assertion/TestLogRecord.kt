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
package io.github.logrecorder.assertion

import io.github.logrecorder.api.LogEntry
import io.github.logrecorder.api.LogLevel
import io.github.logrecorder.api.LogRecord

data class TestLogRecord(override val entries: List<LogEntry>) : LogRecord

fun logRecord(vararg entries: LogEntry): LogRecord =
    TestLogRecord(listOf(*entries))

fun logEntry(
    level: LogLevel = LogLevel.INFO,
    message: String = "message",
    logger: String = "logger",
    marker: String? = null,
    properties: Map<String, String> = emptyMap(),
    throwable: Throwable? = null
) = LogEntry(logger, level, message, marker, properties, throwable)
