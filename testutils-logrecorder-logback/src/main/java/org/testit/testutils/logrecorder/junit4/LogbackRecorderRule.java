package org.testit.testutils.logrecorder.junit4;

import static java.util.stream.Collectors.toList;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.junit.Rule;
import org.junit.rules.ExternalResource;
import org.slf4j.LoggerFactory;
import org.testit.testutils.logrecorder.api.LogRecord;
import org.testit.testutils.logrecorder.internal.logback.LogbackLogRecorder;

import ch.qos.logback.classic.Logger;

import org.testit.testutils.logrecorder.internal.logback.LogbackLog;


/**
 * This {@link Rule} can be used to record (Logback) logging while executing tests.
 * The recorded logs can be retrieved by calling {@link #getLog()}.
 * <p>
 * In order to specify which logs should be recorded you must provide either the classes which are logged, or the names of
 * the loggers.
 *
 * @see LogRecord
 */
public class LogbackRecorderRule extends ExternalResource {

    private final LogbackLog log;
    private final List<LogbackLogRecorder> recorders;

    /**
     * Creates a new {@link LogbackRecorderRule} for the given classes. The classes' name will be used as the logger name to
     * record.
     *
     * @param loggersToRecord the loggers to record
     */
    public LogbackRecorderRule(Class<?>... loggersToRecord) {
        this(getLoggers(loggersToRecord));
    }

    /**
     * Creates a new {@link LogbackRecorderRule} for the given logger names.
     *
     * @param loggersToRecord the loggers to record
     */
    public LogbackRecorderRule(String... loggersToRecord) {
        this(getLoggers(loggersToRecord));
    }

    private LogbackRecorderRule(Stream<org.slf4j.Logger> loggersToWatch) {
        log = new LogbackLog();
        recorders = loggersToWatch//
            .map(logger -> ( Logger ) logger)//
            .map(logger -> new LogbackLogRecorder(logger, log))//
            .collect(toList());
    }

    @Override
    protected void before() {
        recorders.forEach(LogbackLogRecorder::start);
    }

    @Override
    protected void after() {
        recorders.forEach(LogbackLogRecorder::stop);
    }

    /**
     * Returns the {@link LogRecord} of this instance.
     *
     * @return the log record
     */
    public LogRecord getLog() {
        return log;
    }

    private static Stream<org.slf4j.Logger> getLoggers(Class<?>[] watchedLoggers) {
        return Arrays.stream(watchedLoggers).map(LoggerFactory::getLogger);
    }

    private static Stream<org.slf4j.Logger> getLoggers(String[] watchedLoggers) {
        return Arrays.stream(watchedLoggers).map(LoggerFactory::getLogger);
    }

}
