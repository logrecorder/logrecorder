package info.novatec.testit.logrecorder.api

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
     * Returns a list of all recorded log messages up to this point.
     *
     * @see LogEntry.message
     * @since 1.0
     */
    val messages: List<String>
        get() = entries.map { it.message }

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
     * @since 1.0
     */
    fun entries(logger: String? = null, level: LogLevel? = null): List<LogEntry> = entries
        .filter { if (logger != null) it.logger == logger else true }
        .filter { if (level != null) it.level == level else true }

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
     * @since 1.0
     */
    fun messages(logger: String? = null, level: LogLevel? = null): List<String> = entries
        .filter { if (logger != null) it.logger == logger else true }
        .filter { if (level != null) it.level == level else true }
        .map { it.message }

    companion object {
        /**
         * Generates the logger name of the given [KClass] to be used when filtering
         * [log entries][LogEntry] or messages.
         *
         * @see LogRecord.entries
         * @see LogRecord.messages
         * @since 1.0
         */
        @JvmStatic
        fun logger(clazz: KClass<*>): String = logger(clazz.java)

        @JvmStatic
        fun logger(clazz: Class<*>): String = clazz.name
    }

}
