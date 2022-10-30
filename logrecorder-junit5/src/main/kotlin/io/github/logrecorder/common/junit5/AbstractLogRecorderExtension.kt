package io.github.logrecorder.common.junit5

import io.github.logrecorder.api.LogRecord
import io.github.logrecorder.api.MutableLogRecord
import io.github.logrecorder.common.LogRecorder
import io.github.logrecorder.common.LogRecorderExecutionBase
import org.junit.jupiter.api.extension.*
import org.junit.jupiter.api.extension.ExtensionContext.Namespace
import org.junit.jupiter.api.extension.ExtensionContext.Store
import java.lang.reflect.Method

abstract class AbstractLogRecorderExtension<L : Any, LR : MutableLogRecord<*>> : LogRecorderExecutionBase<L, LR>(),
    BeforeTestExecutionCallback, AfterTestExecutionCallback, ParameterResolver {

    private val namespace = Namespace.create(javaClass)

    override fun supportsParameter(parameterContext: ParameterContext, extensionContext: ExtensionContext) =
        LogRecord::class.java == parameterContext.parameter.type

    override fun resolveParameter(parameterContext: ParameterContext, extensionContext: ExtensionContext) =
        extensionContext.logRecord

    override fun beforeTestExecution(context: ExtensionContext) {
        val logRecord = createLogRecord()
        context.logRecord = logRecord

        val recorders = getLoggers(context.requiredTestMethod)
            .map { createLogRecorder(it, logRecord) }
            .onEach { it.start() }
        context.recorders = recorders
    }

    protected abstract fun getLoggers(testMethod: Method): Set<L>

    override fun afterTestExecution(context: ExtensionContext) {
        context.recorders.forEach { it.stop() }
    }

    @Suppress("UNCHECKED_CAST")
    private var ExtensionContext.recorders: List<LogRecorder>
        get() = store.get("log-recorders") as List<LogRecorder>
        set(value) = store.put("log-recorders", value)

    private var ExtensionContext.logRecord: LogRecord
        get() = store.get("log-record", LogRecord::class.java)
        set(value) = store.put("log-record", value)

    private val ExtensionContext.store: Store
        get() = getStore(namespace)

}
