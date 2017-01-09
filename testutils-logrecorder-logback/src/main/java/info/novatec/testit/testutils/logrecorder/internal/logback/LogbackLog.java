package info.novatec.testit.testutils.logrecorder.internal.logback;

import ch.qos.logback.classic.spi.ILoggingEvent;

import info.novatec.testit.testutils.logrecorder.api.LogLevel;
import info.novatec.testit.testutils.logrecorder.internal.AbstractLog;


public class LogbackLog extends AbstractLog<ILoggingEvent> {

    @Override
    public void record(ILoggingEvent value) {
        String loggerName = value.getLoggerName();
        LogLevel logLevel = LogLevelConverter.convert(value.getLevel());
        String message = value.getFormattedMessage();
        record(loggerName, logLevel, message);
    }

}
