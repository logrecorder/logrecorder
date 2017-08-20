package org.testit.testutils.logrecorder.assertj;

import java.util.List;
import java.util.stream.Collectors;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ListAssert;
import org.testit.testutils.logrecorder.api.LogEntry;
import org.testit.testutils.logrecorder.api.LogRecord;


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
        List<String> messages = actual.getEntries()//
            .map(LogEntry::getMessage)//
            .collect(Collectors.toList());
        return Assertions.assertThat(messages);
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
