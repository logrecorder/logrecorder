package org.testit.testutils.logrecorder.internal.logback;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.atIndex;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.testit.testutils.logrecorder.api.LogEntry;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;


public class LogbackLogRecorderIntTest {

    Logger logger;
    LogbackLog log;
    LogbackLogRecorder cut;

    @BeforeEach
    void setUp() {
        logger = ( Logger ) LoggerFactory.getLogger(LogbackLogRecorderIntTest.class);
        logger.setLevel(Level.INFO);
        log = new LogbackLog();
        cut = new LogbackLogRecorder(logger, log);
    }

    @Test
    void startingTheRecordingSetsLogLevelToLowestValue() {
        cut.start();
        assertThat(logger.getLevel()).isEqualTo(Level.ALL);
    }

    @Test
    void startingTheRecordingRecordsAllLogging() {
        cut.start();
        logger.trace("trace message");
        logger.debug("debug message");
        logger.info("info message");
        logger.warn("warn message");
        logger.error("error message");
        Assertions.assertThat(log.getEntries()).extracting(LogEntry::getMessage)//
            .contains("trace message", atIndex(0))
            .contains("debug message", atIndex(1))
            .contains("info message", atIndex(2))
            .contains("warn message", atIndex(3))
            .contains("error message", atIndex(4))
            .hasSize(5);
    }

    @Test
    void stoppingTheRecordingRestoresOriginalLogLevel() {
        cut.start();
        assertThat(logger.getLevel()).isEqualTo(Level.ALL);
        cut.stop();
        assertThat(logger.getLevel()).isEqualTo(Level.INFO);
    }

    @Test
    void stoppingTheRecordingNoLongerRecordsLogging() {
        cut.start();
        logger.info("msg #1");
        logger.info("msg #2");
        cut.stop();
        logger.info("msg #3");
        Assertions.assertThat(log.getEntries()).extracting(LogEntry::getMessage)//
            .contains("msg #1", atIndex(0))//
            .contains("msg #2", atIndex(1))//
            .hasSize(2);
    }

}
