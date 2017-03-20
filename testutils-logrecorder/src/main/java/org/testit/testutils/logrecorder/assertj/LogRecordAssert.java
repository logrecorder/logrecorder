package org.testit.testutils.logrecorder.assertj;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ListAssert;
import org.testit.testutils.logrecorder.api.LogRecord;

import org.testit.testutils.logrecorder.api.LogEntry;


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
        return Assertions.assertThat(actual.getEntries()).extracting(LogEntry::getMessage);
    }

    /**
     * Asserts that the {@link LogRecord} has a certain size (= number of {@link LogEntry entries}).
     *
     * @param size the expected size
     * @return the same {@link LogRecordAssert} for fluent API use
     */
    public LogRecordAssert hasSize(int size) {
        Assertions.assertThat(actual.getEntries()).hasSize(size);
        return this;
    }

}
