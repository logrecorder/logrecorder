package org.testit.testutils.logrecorder.api;

import lombok.Data;
import lombok.NonNull;


/**
 * Represents a single log entry. Includes the following data:
 * <ul>
 * <li>the name of the logger ({@link #loggerName})</li>
 * <li>the level on which the entry was logged ({@link #level})</li>
 * <li>the (formatted) message ({@link #message})</li>
 * </ul>
 *
 * @see LogLevel
 */
@Data
public class LogEntry {
    @NonNull
    private final String loggerName;
    @NonNull
    private final LogLevel level;
    @NonNull
    private final String message;
}
