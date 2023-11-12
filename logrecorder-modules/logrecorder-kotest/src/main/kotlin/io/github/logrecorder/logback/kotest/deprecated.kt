package io.github.logrecorder.logback.kotest

import io.github.logrecorder.api.LogRecord
import io.github.logrecorder.kotest.LogRecorderExtension
import io.github.logrecorder.kotest.recordLogs
import kotlin.reflect.KClass

/**
 * Convenience Method to instantiate [LogRecorderExtension]
 * This extension will record the loggers specified by the parameters save the [LogRecord] into the CoroutineContext
 * for retrieval by logRecord
 *
 * Recording a logger will set its log level to ALL for the duration of the test.
 * After the test was executed, the log level will be restored to whatever it was before.
 *
 * @param classes [KClass] whose name should be used to identify loggers to record.
 * @param names String names of the logger instances to record
 * @see LogRecorderExtension
 * @since 2.4
 */
@Deprecated(
    message = "relocated to new package",
    replaceWith = ReplaceWith(
        "recordLogs",
        "io.github.logrecorder.kotest.recordLogs"
    )
)
fun recordLogs(
    vararg classes: KClass<*>,
    names: Array<out String> = emptyArray()
) = recordLogs(classes = classes.toSet(), names = names.toSet())

/**
 * @see recordLogs
 * @since 2.4
 */
@Deprecated(
    message = "relocated to new package",
    replaceWith = ReplaceWith(
        "recordLogs",
        "io.github.logrecorder.kotest.recordLogs"
    )
)
fun recordLogs(
    vararg names: String,
) = recordLogs(classes = emptySet(), names = names.toSet())
