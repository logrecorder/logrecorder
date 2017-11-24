package org.testit.testutils.logrecorder.api;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.stream.Collectors;
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

    default Stream<LogEntry> getEntries(LogLevel level) {
        return getEntries().filter(entry -> entry.getLevel().equals(level));
    }

    default Stream<LogEntry> getEntries(String logger) {
        return getEntries().filter(entry -> entry.getLoggerName().equals(logger));
    }

    default Stream<LogEntry> getEntries(Class<?> logger) {
        return getEntries().filter(entry -> entry.getLoggerName().equals(logger.getName()));
    }

    default Stream<LogEntry> getEntries(String logger, LogLevel level) {
        return getEntries(logger).filter(entry -> entry.getLevel().equals(level));
    }

    default Stream<LogEntry> getEntries(Class<?> logger, LogLevel level) {
        return getEntries(logger).filter(entry -> entry.getLevel().equals(level));
    }

    default List<String> getMessages() {
        return getEntries().map(LogEntry::getMessage).collect(toList());
    }

    default List<String> getMessages(LogLevel level) {
        return getEntries(level).map(LogEntry::getMessage).collect(toList());
    }

    default List<String> getMessages(String logger) {
        return getEntries(logger).map(LogEntry::getMessage).collect(toList());
    }

    default List<String> getMessages(Class<?> logger) {
        return getEntries(logger).map(LogEntry::getMessage).collect(toList());
    }

    default List<String> getMessages(String logger, LogLevel level) {
        return getEntries(logger, level).map(LogEntry::getMessage).collect(toList());
    }

    default List<String> getMessages(Class<?> logger, LogLevel level) {
        return getEntries(logger, level).map(LogEntry::getMessage).collect(toList());
    }

}
