package info.novatec.testit.testutils.logrecorder.api;

/**
 * All the supported log levels. This should cover most of the logging frameworks used today. Any level not matching one of
 * these should be mapped to {@link #UNKNOWN}.
 */
public enum LogLevel {
    UNKNOWN,
    TRACE,
    DEBUG,
    INFO,
    WARN,
    ERROR;
}
