package io.github.logrecorder.logback.junit5

import io.github.logrecorder.junit5.LogRecorderExtension
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.reflect.KClass

/**
 * Annotating any test method with this annotation will start the recording of
 * log messages while the test method is executed.
 *
 * Only those loggers configured with this annotation - either by its class or
 * name - will be recorded! Multiple loggers can be set by either method and
 * combinations between class and explicit names are possible as well.
 *
 * @see value
 * @see names
 * @since 1.0
 */
@Retention
@Deprecated(
    message = "replaced with new central annotation",
    replaceWith = ReplaceWith(
        "RecordLoggers",
        "io.github.logrecorder.junit5.RecordLoggers"
    )
)
@Target(AnnotationTarget.FUNCTION)
@ExtendWith(LogRecorderExtension::class)
annotation class RecordLoggers(

    /**
     * Classes whose name should be used to identify loggers to record.
     */
    vararg val value: KClass<*> = [],

    /**
     * Names of loggers to record.
     */
    val names: Array<String> = []

)
