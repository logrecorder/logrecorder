package info.novatec.testit.logrecorder.api

/**
 * All the supported log levels.
 *
 * This should cover most of the logging frameworks used today.
 * Any level not matching one of these should be mapped to [UNKNOWN].
 *
 * @since 1.0
 */
enum class LogLevel {
    TRACE, DEBUG, INFO, WARN, ERROR, UNKNOWN
}
