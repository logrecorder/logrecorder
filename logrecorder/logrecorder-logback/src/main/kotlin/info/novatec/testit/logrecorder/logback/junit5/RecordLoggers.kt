package info.novatec.testit.logrecorder.logback.junit5

import org.junit.jupiter.api.extension.ExtendWith
import kotlin.reflect.KClass

/**
 * Annotating any test method with this annotation will start the recording of
 * log messages while the test method is executed.
 *
 * Only those loggers configured with this annotation - either by it's class or
 * name - will be recorded! Multiple loggers can be set by either method and
 * combinations between class and explicit names are possible as well.
 *
 * @see value
 * @see names
 * @since 1.0
 */
@Retention
@Target(AnnotationTarget.FUNCTION)
@ExtendWith(LogbackRecorderExtension::class)
annotation class RecordLoggers(

    /**
     * Classes who's name should be used to identify loggers to record.
     */
    vararg val value: KClass<*> = [],

    /**
     * Names of loggers to record.
     */
    val names: Array<String> = []

)
