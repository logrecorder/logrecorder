package io.github.logrecorder.jul.programmatic

import io.github.logrecorder.api.LogRecord
import io.github.logrecorder.programmatic.recordLoggers
import kotlin.reflect.KClass

/**
 * This support function will wrap the execution block and record the loggers specified by the parameters
 * classes and/or names and provide the [LogRecord] to the execution block.
 *
 * Recording a logger will set its log level to include everything for the duration of the test.
 * After the test was executed, the log level will be restored to whatever it was before.
 *
 * @param classes [KClass] whose name should be used to identify loggers to record.
 * @param names String names of the logger instances to record
 * @see recordLoggers
 * @see LogRecord
 * @since 2.4
 */
@Deprecated(
    message = "replaced wth central implementation",
    replaceWith = ReplaceWith(
        "recordLoggers",
        "io.github.logrecorder.programmatic.recordLoggers"
    )
)
fun <T : Any> recordLoggers(
    vararg classes: KClass<*>,
    names: Array<out String> = emptyArray(),
    block: (LogRecord) -> T
) = recordLoggers(classes = classes.toSet(), names = names.toSet(), block = block)

/**
 * @see recordLoggers
 * @since 2.4
 */
@Deprecated(
    message = "replaced wth central implementation",
    replaceWith = ReplaceWith(
        "recordLoggers",
        "io.github.logrecorder.programmatic.recordLoggers"
    )
)
fun <T : Any> recordLoggers(
    vararg names: String,
    block: (LogRecord) -> T
) = recordLoggers(classes = emptySet(), names = names.toSet(), block = block)
