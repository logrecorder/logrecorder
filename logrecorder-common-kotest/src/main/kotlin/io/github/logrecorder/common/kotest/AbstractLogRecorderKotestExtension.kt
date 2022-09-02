package io.github.logrecorder.common.kotest

import io.github.logrecorder.api.LogRecord
import io.github.logrecorder.api.MutableLogRecord
import io.github.logrecorder.common.LogRecorder
import io.github.logrecorder.common.LogRecorderExecutionBase
import io.kotest.core.extensions.TestCaseExtension
import io.kotest.core.listeners.AfterInvocationListener
import io.kotest.core.listeners.BeforeInvocationListener
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult
import io.kotest.core.test.TestScope
import io.kotest.core.test.TestType
import kotlinx.coroutines.withContext
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.coroutineContext
import kotlin.reflect.KClass

class LogRecordContextCoroutineContextElement(val value: LogRecord) : AbstractCoroutineContextElement(Key) {
    companion object Key : CoroutineContext.Key<LogRecordContextCoroutineContextElement>
}

private val CoroutineContext.internalLogRecord: LogRecord
    get() = this[LogRecordContextCoroutineContextElement]?.value
        ?: error("No LogRecord defined in this coroutine context")

/**
 * Returns the [LogRecord] from a test.
 */
val TestScope.logRecord: LogRecord
    get() = coroutineContext.internalLogRecord

abstract class AbstractLogRecorderKotestExtension<L : Any, LR : MutableLogRecord<*>>(
    private val classes: Array<out KClass<*>> = emptyArray(),
    private val names: Array<out String> = emptyArray()
) : LogRecorderExecutionBase<L, LR>(), TestCaseExtension, BeforeInvocationListener, AfterInvocationListener {

    lateinit var recorders: List<LogRecorder>

    override suspend fun intercept(testCase: TestCase, execute: suspend (TestCase) -> TestResult): TestResult {
        if (testCase.isContainer()) return execute(testCase)

        return withContext(LogRecordContextCoroutineContextElement(createLogRecord())) {
            return@withContext execute(testCase)
        }
    }

    @Suppress("UNCHECKED_CAST")
    override suspend fun beforeInvocation(testCase: TestCase, iteration: Int) {
        val logRecord = coroutineContext.internalLogRecord as LR
        recorders = getLoggers(classes, names)
            .map { createLogRecorder(it, logRecord) }
            .onEach { it.start() }
    }

    override suspend fun afterInvocation(testCase: TestCase, iteration: Int) {
        recorders.forEach { it.stop() }
    }
}

private fun TestCase.isContainer() = type == TestType.Container
