/*
 * Copyright 2017-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package info.novatec.testit.logrecorder.jul.junit5

import info.novatec.testit.logrecorder.api.LogRecord
import info.novatec.testit.logrecorder.jul.JulLogRecord
import info.novatec.testit.logrecorder.jul.JulLogRecorder
import org.junit.jupiter.api.extension.*
import java.util.logging.LogManager
import java.util.logging.Logger

/**
 * This extension will record the loggers specified by a [RecordLoggers] annotation and inject the [LogRecord]
 * as a parameter into the test method.
 *
 * Recording a logger will set its log level to [java.util.logging.Level.FINER] for the duration of the test. After the
 * test was executed, the log level will be restored to whatever it was before.
 *
 * @see RecordLoggers
 * @see LogRecord
 * @since 1.5
 */
class JulRecorderExtension : BeforeTestExecutionCallback, AfterTestExecutionCallback,
    ParameterResolver {

    private val namespace = ExtensionContext.Namespace.create(javaClass)

    override fun supportsParameter(
        parameterContext: ParameterContext,
        extensionContext: ExtensionContext
    ): Boolean = LogRecord::class.java == parameterContext.parameter.type

    override fun resolveParameter(
        parameterContext: ParameterContext,
        extensionContext: ExtensionContext
    ): Any = extensionContext.logRecord

    override fun beforeTestExecution(context: ExtensionContext) {
        val annotation = context.requiredTestMethod.getAnnotation(RecordLoggers::class.java)
            ?: error("no @RecordLoggers annotation found on test method!")
        val fromClasses = annotation.value.map { LogManager.getLogManager().getLogger(it.java.name) }
        val fromNames = annotation.names.map { LogManager.getLogManager().getLogger(it) }

        val logRecord = JulLogRecord()
        context.logRecord = logRecord

        val recorders = (fromClasses + fromNames)
            .distinct()
            .map { it as Logger }
            .map { JulLogRecorder(it, logRecord) }
            .onEach { it.start() }
        context.recorders = recorders
    }

    override fun afterTestExecution(context: ExtensionContext) {
        context.recorders.forEach { it.stop() }
    }

    @Suppress("UNCHECKED_CAST")
    private var ExtensionContext.recorders: List<JulLogRecorder>
        get() = store.get("log-recorders") as List<JulLogRecorder>
        set(value) = store.put("log-recorders", value)

    private var ExtensionContext.logRecord: JulLogRecord
        get() = store.get("log-record", JulLogRecord::class.java)
        set(value) = store.put("log-record", value)

    private val ExtensionContext.store: ExtensionContext.Store
        get() = getStore(namespace)

}
