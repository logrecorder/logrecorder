package de.novatec.logrecorder.api


/**
 * Contains the data of a single entry of the recorded log.
 *
 * Includes the following data:
 *
 * - the name of the logger ([logger])
 * - the [level][LogLevel] on which the entry was logged ([level])
 * - the (formatted) message ([message])
 *
 * @since 1.0
 * @see LogLevel
 */
data class LogEntry(
    val logger: String,
    val level: LogLevel,
    val message: String
)
