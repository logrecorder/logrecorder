package info.novatec.testit.testutils.logrecorder.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;


public class LogEntryTest {

    @Nested
    @DisplayName("equality")
    class Equality {

        @Test
        @DisplayName("identical values makes entries equal")
        void sameValues() {
            LogEntry entry1 = new LogEntry("foo.Bar", LogLevel.INFO, "a message");
            LogEntry entry2 = new LogEntry("foo.Bar", LogLevel.INFO, "a message");
            assertThat(entry1).isEqualTo(entry2);
        }

        @Test
        @DisplayName("different logger name makes entries unequal")
        void differentLogger() {
            LogEntry entry1 = new LogEntry("foo.Bar", LogLevel.INFO, "a message");
            LogEntry entry2 = new LogEntry("foo.Xur", LogLevel.INFO, "a message");
            assertThat(entry1).isNotEqualTo(entry2);
        }

        @Test
        @DisplayName("different log level makes entries unequal")
        void differentLevel() {
            LogEntry entry1 = new LogEntry("foo.Bar", LogLevel.INFO, "a message");
            LogEntry entry2 = new LogEntry("foo.Bar", LogLevel.DEBUG, "a message");
            assertThat(entry1).isNotEqualTo(entry2);
        }

        @Test
        @DisplayName("different message makes entries unequal")
        void differentMessage() {
            LogEntry entry1 = new LogEntry("foo.Bar", LogLevel.INFO, "a message");
            LogEntry entry2 = new LogEntry("foo.Bar", LogLevel.INFO, "another message");
            assertThat(entry1).isNotEqualTo(entry2);
        }

    }

    @Nested
    @DisplayName("all values are required to be non-null")
    class AllValuesAreRequired {

        @Test
        @DisplayName("logger name")
        void loggerName() {
            assertThrows(NullPointerException.class, () -> {
                new LogEntry(null, LogLevel.INFO, "a message");
            });
        }

        @Test
        @DisplayName("log level")
        void logLevel() {
            assertThrows(NullPointerException.class, () -> {
                new LogEntry("foo.Bar", null, "a message");
            });
        }

        @Test
        @DisplayName("message")
        void message() {
            assertThrows(NullPointerException.class, () -> {
                new LogEntry("foo.Bar", LogLevel.INFO, null);
            });
        }

    }

}
