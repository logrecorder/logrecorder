package io.github.logrecorder.common

import io.github.logrecorder.api.LogRecord
import java.util.ServiceLoader
import kotlin.reflect.KClass

/**
 * Records the specified loggers while executing the given [block].
 *
 * @param T Return type of the execution block
 * @param classes [KClass] whose name should be used to identify loggers to record.
 * @param block code block to execute
 * @return optional return of the executed code block's result
 * @since 2.9
 */
fun <T> recordLoggers(
    vararg classes: KClass<*>,
    block: (LogRecord) -> T
) = recordLoggers(classes.toSet(), emptySet(), block)

/**
 * Records the specified loggers while executing the given [block].
 *
 * @param T Return type of the execution block
 * @param names String names of the Logger (specific to logging implementation) instances to record
 * @param block code block to execute
 * @return optional return of the executed code block's result
 * @since 2.9
 */
fun <T> recordLoggers(
    vararg names: String,
    block: (LogRecord) -> T
) = recordLoggers(emptySet(), names.toSet(), block)

/**
 * Records the specified loggers while executing the given [block].
 *
 * @param T Return type of the execution block
 * @param classes [KClass] whose name should be used to identify loggers to record.
 * @param names String names of the Logger (specific to logging implementation) instances to record
 * @param block code block to execute
 * @return optional return of the executed code block's result
 * @since 2.9
 */
fun <T> recordLoggers(
    classes: Collection<KClass<*>> = emptySet(),
    names: Collection<String> = emptySet(),
    block: (LogRecord) -> T
): T {
    val factory = ServiceLoader.load(LogRecorderContextFactory::class.java).single()
    val context = factory.create(classes.toSet(), names.toSet())

    try {
        context.start()
        return block(context.record)
    } finally {
        context.stop()
    }
}
