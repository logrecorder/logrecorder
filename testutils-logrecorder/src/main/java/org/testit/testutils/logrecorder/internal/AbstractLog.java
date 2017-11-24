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

}
