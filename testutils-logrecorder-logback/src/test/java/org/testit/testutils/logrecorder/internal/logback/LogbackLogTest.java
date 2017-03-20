package org.testit.testutils.logrecorder.internal.logback;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.testit.testutils.logrecorder.api.LogEntry;
import org.testit.testutils.logrecorder.api.LogLevel;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.LoggingEvent;


public class LogbackLogTest {

    LogbackLog cut = new LogbackLog();

    @Test
    @DisplayName("recording an ILoggingEvent creates a LogEntry")
    void recordingAnEventCreatesLogEntry() {

        LoggingEvent event = new LoggingEvent();
        event.setLevel(Level.INFO);
        event.setLoggerName("foo.Bar");
        event.setMessage("a message");

        cut.record(event);

        LogEntry entry = cut.getEntries().findFirst().get();
        assertThat(entry.getLoggerName()).isEqualTo("foo.Bar");
        assertThat(entry.getLevel()).isSameAs(LogLevel.INFO);
        assertThat(entry.getMessage()).isEqualTo("a message");

    }

}
