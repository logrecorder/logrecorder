package org.testit.testutils.logrecorder.junit5;

import static org.assertj.core.api.Assertions.atIndex;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testit.testutils.logrecorder.api.LogRecord;
import org.testit.testutils.logrecorder.assertj.LogRecorderAssertions;


public class LogWatcherExtensionIntTest {

    static final String LOGGING_CLASS_NAME =
        "org.testit.testutils.logrecorder.junit5.LogWatcherExtensionIntTest$LoggingClass";
    static final String ANOTHER_LOGGING_CLASS_NAME =
        "org.testit.testutils.logrecorder.junit5.LogWatcherExtensionIntTest$AnotherLoggingClass";

    LoggingClass loggingClass1 = new LoggingClass();
    AnotherLoggingClass loggingClass2 = new AnotherLoggingClass();

    @Test
    @RecordLoggers({ LoggingClass.class, AnotherLoggingClass.class })
    void recordedLoggersCanBeDeclaredByClassReference(LogRecord record) {
        loggingClass1.doSomeThing();
        LogRecorderAssertions.assertThat(record)
            .messages()
            .contains("trace log", atIndex(0))
            .contains("debug log", atIndex(1))
            .contains("info log", atIndex(2))
            .contains("warning log", atIndex(3))
            .contains("error log", atIndex(4));
        loggingClass2.doSomeThing();
        LogRecorderAssertions.assertThat(record).messages()//
            .contains("another info log", atIndex(5))//
            .hasSize(6);
    }

    @Test
    @RecordLoggers(names = { LOGGING_CLASS_NAME, ANOTHER_LOGGING_CLASS_NAME })
    void recordedLoggersCanBeDeclaredByName(LogRecord record) {
        loggingClass1.doSomeThing();
        LogRecorderAssertions.assertThat(record)
            .messages()
            .contains("trace log", atIndex(0))
            .contains("debug log", atIndex(1))
            .contains("info log", atIndex(2))
            .contains("warning log", atIndex(3))
            .contains("error log", atIndex(4));
        loggingClass2.doSomeThing();
        LogRecorderAssertions.assertThat(record).messages()//
            .contains("another info log", atIndex(5))//
            .hasSize(6);
    }

    @Test
    @RecordLoggers(value = LoggingClass.class, names = ANOTHER_LOGGING_CLASS_NAME)
    void declaringByClassAndNameWillUseBoth(LogRecord record) {
        loggingClass1.doSomeThing();
        loggingClass2.doSomeThing();
        LogRecorderAssertions.assertThat(record).hasSize(6);
    }

    static class LoggingClass {
        Logger logger = LoggerFactory.getLogger(getClass());

        void doSomeThing() {
            logger.trace("trace log");
            logger.debug("debug log");
            logger.info("info log");
            logger.warn("warning log");
            logger.error("error log");
        }
    }

    static class AnotherLoggingClass {
        Logger logger = LoggerFactory.getLogger(getClass());

        void doSomeThing() {
            logger.info("another info log");
        }
    }

}
