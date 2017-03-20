package org.testit.testutils.logrecorder.internal.logback;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;


public class LogbackLogRecorder {

    private final Logger logger;
    private final Level originalLevel;
    private final Appender<ILoggingEvent> appender;

    public LogbackLogRecorder(Logger logger, LogbackLog log) {
        this.logger = logger;
        this.originalLevel = logger.getEffectiveLevel();
        this.appender = new CallbackAppender("temporary log appender #" + System.nanoTime(), log::record);
    }

    public void start() {
        logger.setLevel(Level.ALL);
        logger.addAppender(appender);
    }

    public void stop() {
        logger.detachAppender(appender);
        logger.setLevel(originalLevel);
    }

}
