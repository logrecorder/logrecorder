package info.novatec.testit.testutils.logrecorder.api;

import java.util.stream.Stream;


/**
 * A recording of one or multiple loggers.
 * <p>
 * You can either retrieve all {@link LogEntry log entries} or provide a {@link LogLevel} or logger name in order to get a
 * pre-filtered stream of entries.
 * <p>
 * This is the primary way of reading a log recording.
 */
public interface LogRecord {

    Stream<LogEntry> getEntries();

    Stream<LogEntry> getEntries(LogLevel level);

    Stream<LogEntry> getEntries(String logger);

    Stream<LogEntry> getEntries(Class<?> logger);

    Stream<LogEntry> getEntries(String logger, LogLevel level);

    Stream<LogEntry> getEntries(Class<?> logger, LogLevel level);

}
