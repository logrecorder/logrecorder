package org.testit.testutils.logrecorder.assertj;

import org.testit.testutils.logrecorder.api.LogRecord;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;


/**
 * Provides factory methods for all assertions related to the recording of logs.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class LogRecorderAssertions {

    /**
     * Creates a new {@link LogRecordAssert} for the given {@link LogRecord}.
     *
     * @param record the instance to be asserted
     * @return the assertion
     */
    public static LogRecordAssert assertThat(LogRecord record) {
        return new LogRecordAssert(record);
    }

}
