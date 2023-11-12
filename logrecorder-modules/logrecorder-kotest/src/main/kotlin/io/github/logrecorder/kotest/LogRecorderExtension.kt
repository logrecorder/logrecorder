package io.github.logrecorder.kotest

import io.github.logrecorder.api.LogRecord
import io.github.logrecorder.common.LogRecorderContext
import io.github.logrecorder.common.LogRecorderContextFactory
import io.github.logrecorder.jul.kotest.recordLogs
import io.kotest.core.extensions.TestCaseExtension
import io.kotest.core.listeners.AfterInvocationListener
import io.kotest.core.listeners.BeforeInvocationListener
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult
import io.kotest.core.test.TestScope
import io.kotest.core.test.TestType
import kotlinx.coroutines.withContext
import java.util.ServiceLoader
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.CoroutineContext.Key
import kotlin.coroutines.coroutineContext
import kotlin.reflect.KClass

/**
 * Returns the [LogRecord] from the coroutine context of the test.
 */
val TestScope.logRecord: LogRecord
    get() = coroutineContext.logRecorderContext.record

/**
 * Convenience Method to instantiate [LogRecorderExtension]
 * This extension will record the loggers specified by the parameters save the [LogRecord] into the CoroutineContext
 * for retrieval by logRecord
 *
 * Recording a logger will set its log level to include all messages for the duration of the test.
 * After the test was executed, the log level will be restored to whatever it was before.
 *
 * @param classes [KClass] whose name should be used to identify loggers to record.
 * @param names String names of the logger instances to record
 * @see LogRecorderExtension
 * @since 2.9
 */
fun recordLogs(
    vararg classes: KClass<*>,
    names: Array<out String> = emptyArray()
) = LogRecorderExtension(classes = classes, names = names)

/**
 * @see recordLogs
 * @since 2.9
 */
fun recordLogs(
    vararg names: String,
) = LogRecorderExtension(classes = emptyArray(), names = names)

class LogRecorderExtension(
    private val classes: Array<out KClass<*>> = emptyArray(),
    private val names: Array<out String> = emptyArray()
) : TestCaseExtension, BeforeInvocationListener, AfterInvocationListener {

    private val factory = ServiceLoader.load(LogRecorderContextFactory::class.java).single()

    override suspend fun intercept(testCase: TestCase, execute: suspend (TestCase) -> TestResult): TestResult {
        if (testCase.isContainer()) return execute(testCase)

        val context = factory.create(classes.toSet(), names.toSet())

        return withContext(LogRecorderContextCoroutineContextElement(context)) {
            return@withContext execute(testCase)
        }
    }

    override suspend fun beforeInvocation(testCase: TestCase, iteration: Int) {
        coroutineContext.logRecorderContext.start()
    }

    override suspend fun afterInvocation(testCase: TestCase, iteration: Int) {
        coroutineContext.logRecorderContext.stop()
    }
}

private fun TestCase.isContainer() = type == TestType.Container

private object ContextKey : Key<LogRecorderContextCoroutineContextElement>
private class LogRecorderContextCoroutineContextElement(val value: LogRecorderContext) :
    AbstractCoroutineContextElement(ContextKey)

internal val CoroutineContext.logRecorderContext: LogRecorderContext
    get() = this[ContextKey]?.value ?: error("No LogRecorderContext defined in this coroutine context")
