package io.github.logrecorder.common.kotest

import io.github.logrecorder.api.LogRecord
import io.github.logrecorder.kotest.logRecorderContext
import io.kotest.core.test.TestScope

/**
 * Returns the [LogRecord] from the coroutine context of the test.
 */
@Deprecated(
    message = "relocated to new package",
    replaceWith = ReplaceWith(
        "logRecord",
        "io.github.logrecorder.kotest.logRecord"
    )
)
val TestScope.logRecord: LogRecord
    get() = coroutineContext.logRecorderContext.record
