package info.novatec.testit.testutils.logrecorder.assertj;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.ListAssert;

import info.novatec.testit.testutils.logrecorder.api.LogEntry;
import info.novatec.testit.testutils.logrecorder.api.LogRecord;


/**
 * Provides assertions for {@link LogRecord} instances.
 *
 * @see LogRecord
 */
public class LogRecordAssert extends AbstractAssert<LogRecordAssert, LogRecord> {

    public LogRecordAssert(LogRecord actual) {
        super(actual, LogRecordAssert.class);
    }

    /**
     * Extracts all messages from the {@link LogRecord} in the order they were logged and returns a {@link ListAssert} for
     * them.
     *
     * @return the list assertion
     */
    public ListAssert<String> messages() {
        return assertThat(actual.getEntries()).extracting(LogEntry::getMessage);
    }

    /**
     * Asserts that the {@link LogRecord} has a certain size (= number of {@link LogEntry entries}).
     *
     * @param size the expected size
     * @return the same {@link LogRecordAssert} for fluent API use
     */
    public LogRecordAssert hasSize(int size) {
        assertThat(actual.getEntries()).hasSize(size);
        return this;
    }

}
