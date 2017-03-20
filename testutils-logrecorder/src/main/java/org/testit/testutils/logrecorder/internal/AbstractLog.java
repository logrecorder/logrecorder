package org.testit.testutils.logrecorder.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.testit.testutils.logrecorder.api.LogEntry;
import org.testit.testutils.logrecorder.api.LogLevel;
import org.testit.testutils.logrecorder.api.LogRecord;


public abstract class AbstractLog<T> implements LogRecord {

    private final List<LogEntry> entries = new ArrayList<>();

    public abstract void record(T value);

    public void record(String loggerName, LogLevel level, String message) {
        record(new LogEntry(loggerName, level, message));
    }

    public void record(LogEntry entry) {
        entries.add(entry);
    }

    @Override
    public Stream<LogEntry> getEntries() {
        return entries.stream();
    }

    @Override
    public Stream<LogEntry> getEntries(LogLevel level) {
        return getEntries().filter(entry -> entry.getLevel().equals(level));
    }

    @Override
    public Stream<LogEntry> getEntries(String logger) {
        return getEntries().filter(entry -> entry.getLoggerName().equals(logger));
    }

    @Override
    public Stream<LogEntry> getEntries(Class<?> logger) {
        return getEntries().filter(entry -> entry.getLoggerName().equals(logger.getName()));
    }

    @Override
    public Stream<LogEntry> getEntries(String logger, LogLevel level) {
        return getEntries(logger).filter(entry -> entry.getLevel().equals(level));
    }

    @Override
    public Stream<LogEntry> getEntries(Class<?> logger, LogLevel level) {
        return getEntries(logger).filter(entry -> entry.getLevel().equals(level));
    }

}
