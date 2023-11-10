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
package io.github.logrecorder.api

import kotlin.reflect.KClass

/**
 * Allows access to the recorded [log entries][LogEntry] of the current context.
 *
 * @since 1.0
 */
interface LogRecord {

    /**
     * Returns a list of all recorded [log entries][LogEntry] up to this point.
     *
     * @since 1.0
     */
    val entries: List<LogEntry>

    /**
     * Returns a list of all recorded [log entries][LogEntry] up to this point
     * for a specific logger (name).
     *
     * The [LogRecord.logger] methods can be used to generate the matching logger name for a
     * given class.
     *
     * @see LogRecord.logger
     * @since 2.2
     */
    fun getEntries(logger: String): List<LogEntry> = getEntries(logger, null)

    /**
     * Returns a list of all recorded [log entries][LogEntry] up to this point
     * with a specific [log level][LogLevel].
     *
     * @since 2.2
     */
    fun getEntries(level: LogLevel): List<LogEntry> = getEntries(null, level)

    /**
     * Returns a list of all recorded [log entries][LogEntry] up to this point.
     *
     * This method allows to filter the returned [log entries][LogEntry] by their logger (name)
     * or [log level][LogLevel]. If neither of those are provided, all [log entries][LogEntry]
     * will be returned.
     *
     * The [LogRecord.logger] methods can be used to generate the matching logger name for a
     * given class.
     *
     * @see LogRecord.logger
     * @since 2.2
     */
    fun getEntries(logger: String?, level: LogLevel?): List<LogEntry> = entries
        .filter { if (logger != null) it.logger == logger else true }
        .filter { if (level != null) it.level == level else true }

    /**
     * Returns a list of all recorded log messages up to this point.
     *
     * @see LogEntry.message
     * @since 1.0
     */
    val messages: List<String>
        get() = entries.map { it.message }

    /**
     * Returns a list of all recorded log messages up to this point
     * for a specific logger (name).
     *
     * The [LogRecord.logger] methods can be used to generate the matching logger name for a
     * given class.
     *
     * @see LogRecord.logger
     * @since 2.2
     */
    fun getMessages(logger: String): List<String> = getMessages(logger, null)

    /**
     * Returns a list of all recorded log messages up to this point
     * with a specific [log level][LogLevel].
     *
     * @since 2.2
     */
    fun getMessages(level: LogLevel): List<String> = getMessages(null, level)

    /**
     * Returns a list of all recorded log messages up to this point.
     *
     * This method allows to filter the returned log messages by their logger (name)
     * or [log level][LogLevel]. If neither of those are provided, all log messages
     * will be returned.
     *
     * The [LogRecord.logger] methods can be used to generate the matching logger name for a
     * given class.
     *
     * @see LogRecord.logger
     * @since 2.2
     */
    fun getMessages(logger: String?, level: LogLevel?): List<String> = getEntries(logger, level)
        .map { it.message }

    /**
     * Returns a list of all recorded log markers up to this point.
     *
     * @see LogEntry.marker
     * @since 1.5
     */
    val markers: List<String?>
        get() = entries.map { it.marker }

    /**
     * Returns a list of all recorded log markers up to this point
     * for a specific logger (name).
     *
     * The [LogRecord.logger] methods can be used to generate the matching logger name for a
     * given class.
     *
     * @see LogRecord.logger
     * @since 2.2
     */
    fun getMarkers(logger: String): List<String> = getMarkers(logger, null)

    /**
     * Returns a list of all recorded log markers up to this point
     * with a specific [log level][LogLevel].
     *
     * @since 2.2
     */
    fun getMarkers(level: LogLevel): List<String> = getMarkers(null, level)

    /**
     * Returns a list of all recorded log markers up to this point.
     *
     * This method allows to filter the returned log messages by their logger (name)
     * or [log level][LogLevel]. If neither of those are provided, all log messages
     * will be returned.
     *
     * The [LogRecord.logger] methods can be used to generate the matching logger name for a
     * given class.
     *
     * @see LogRecord.logger
     * @since 2.2
     */
    fun getMarkers(logger: String?, level: LogLevel?): List<String> = getEntries(logger, level)
        .mapNotNull { it.marker }

    @Deprecated("use getEntries(..)", ReplaceWith("getEntries(logger, level)"))
    fun entries(logger: String? = null, level: LogLevel? = null): List<LogEntry> = getEntries(logger, level)

    @Deprecated("use getMessages(..)", ReplaceWith("getMessages(logger, level)"))
    fun messages(logger: String? = null, level: LogLevel? = null): List<String> = getMessages(logger, level)

    @Deprecated("use getMarkers(..)", ReplaceWith("getMarkers(logger, level)"))
    fun markers(logger: String? = null, level: LogLevel? = null): List<String> = getMarkers(logger, level)

    companion object {
        /**
         * Generates the logger name of the given [KClass] to be used when filtering
         * [log entries][LogEntry] or messages.
         *
         * @see LogRecord.entries
         * @see LogRecord.messages
         * @since 1.0
         */
        fun logger(clazz: KClass<*>): String = logger(clazz.java)

        @JvmStatic
        fun logger(clazz: Class<*>): String = clazz.name
    }

}
