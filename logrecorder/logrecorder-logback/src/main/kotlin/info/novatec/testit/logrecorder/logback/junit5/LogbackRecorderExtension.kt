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
package info.novatec.testit.logrecorder.logback.junit5

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Logger
import info.novatec.testit.logrecorder.api.LogRecord
import info.novatec.testit.logrecorder.logback.LogbackLogRecord
import info.novatec.testit.logrecorder.logback.LogbackLogRecorder
import org.junit.jupiter.api.extension.*
import org.junit.jupiter.api.extension.ExtensionContext.Namespace
import org.junit.jupiter.api.extension.ExtensionContext.Store
import org.slf4j.LoggerFactory

/**
 * This extension will record the loggers specified by a [RecordLoggers] annotation and inject the [LogRecord]
 * as a parameter into the test method.
 *
 * Recording a logger will set its log level to to [Level.ALL] for the duration of the test. After the
 * test was executed, the log level will be restored to whatever it was before.
 *
 * @see RecordLoggers
 * @see LogRecord
 * @since 1.0
 */
class LogbackRecorderExtension : BeforeTestExecutionCallback, AfterTestExecutionCallback, ParameterResolver {

    private val namespace = Namespace.create(javaClass)

    override fun supportsParameter(parameterContext: ParameterContext, extensionContext: ExtensionContext): Boolean {
        return LogRecord::class.java == parameterContext.parameter.type
    }

    override fun resolveParameter(parameterContext: ParameterContext, extensionContext: ExtensionContext): Any {
        return extensionContext.logRecord
    }

    override fun beforeTestExecution(context: ExtensionContext) {
        val annotation = context.requiredTestMethod.getAnnotation(RecordLoggers::class.java)
            ?: error("no @RecordLoggers annotation found on test method!")
        val fromClasses = annotation.value.map { LoggerFactory.getLogger(it.java) }
        val fromNames = annotation.names.map { LoggerFactory.getLogger(it) }

        val logRecord = LogbackLogRecord()
        context.logRecord = logRecord

        val recorders = (fromClasses + fromNames)
            .distinct()
            .map { it as Logger }
            .map { LogbackLogRecorder(it, logRecord) }
            .onEach { it.start() }
        context.recorders = recorders
    }

    override fun afterTestExecution(context: ExtensionContext) {
        context.recorders.forEach { it.stop() }
    }

    private var ExtensionContext.recorders: List<LogbackLogRecorder>
        get() = store.get("log-recorders") as List<LogbackLogRecorder>
        set(value) = store.put("log-recorders", value)

    private var ExtensionContext.logRecord: LogbackLogRecord
        get() = store.get("log-record", LogbackLogRecord::class.java)
        set(value) = store.put("log-record", value)

    private val ExtensionContext.store: Store
        get() = getStore(namespace)

}
