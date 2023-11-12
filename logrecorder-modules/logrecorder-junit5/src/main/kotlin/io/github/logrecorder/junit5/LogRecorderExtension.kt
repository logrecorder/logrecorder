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
package io.github.logrecorder.junit5

import io.github.logrecorder.api.LogRecord
import io.github.logrecorder.common.LogRecorderContext
import io.github.logrecorder.common.LogRecorderContextFactory
import org.junit.jupiter.api.extension.AfterTestExecutionCallback
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.ExtensionContext.Namespace
import org.junit.jupiter.api.extension.ParameterContext
import org.junit.jupiter.api.extension.ParameterResolver
import java.lang.reflect.Method
import java.util.ServiceLoader
import kotlin.reflect.KClass

/**
 * This extension will record the loggers specified by a [RecordLoggers] annotation and inject the [LogRecord]
 * as a parameter into the test method.
 *
 * Recording a logger will set its log level to [Level.ALL] for the duration of the test. After the
 * test was executed, the log level will be restored to whatever it was before.
 *
 * @see RecordLoggers
 * @see LogRecord
 * @since 2.9
 */
internal class LogRecorderExtension : BeforeTestExecutionCallback, AfterTestExecutionCallback, ParameterResolver {

    private val namespace = Namespace.create(javaClass)
    private val factory = ServiceLoader.load(LogRecorderContextFactory::class.java).single()

    override fun beforeTestExecution(context: ExtensionContext) {
        val (byClasses, byNames) = getClassesToLog(context.requiredTestMethod)
        val logRecorderContext = factory.create(byClasses, byNames)
            .also(LogRecorderContext::start)
        context.logRecorderContext = logRecorderContext
    }

    override fun afterTestExecution(context: ExtensionContext) {
        context.logRecorderContext.stop()
    }

    override fun supportsParameter(parameterContext: ParameterContext, extensionContext: ExtensionContext): Boolean =
        LogRecord::class.java == parameterContext.parameter.type

    override fun resolveParameter(parameterContext: ParameterContext, extensionContext: ExtensionContext): LogRecord =
        extensionContext.logRecorderContext.record

    private fun getClassesToLog(testMethod: Method): Pair<Set<KClass<*>>, Set<String>> {
        val annotation = testMethod.getAnnotation(RecordLoggers::class.java)
            ?: error("no @RecordLoggers annotation found on test method!")
        return annotation.value.toSet() to annotation.names.toSet()
    }

    private var ExtensionContext.logRecorderContext: LogRecorderContext
        get() = store.get("log-recorder-context", LogRecorderContext::class.java)
        set(value) = store.put("log-recorder-context", value)

    private val ExtensionContext.store: ExtensionContext.Store
        get() = getStore(namespace)
}
