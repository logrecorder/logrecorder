/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package info.novatec.testit.logrecorder.log4j.junit5

import info.novatec.testit.logrecorder.api.LogRecord
import info.novatec.testit.logrecorder.log4j.Log4jLogRecord
import info.novatec.testit.logrecorder.log4j.Log4jLogRecorder
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.junit.jupiter.api.extension.AfterTestExecutionCallback
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.ParameterContext
import org.junit.jupiter.api.extension.ParameterResolver

/**
 * This extension will record the loggers specified by a [RecordLoggers] annotation and inject the [LogRecord]
 * as a parameter into the test method.
 *
 * Recording a logger will set its log level to [org.apache.logging.log4j.Level.ALL] for the duration of the test. After the
 * test was executed, the log level will be restored to whatever it was before.
 *
 * @see RecordLoggers
 * @see LogRecord
 * @since 1.1
 */
class Log4jRecorderExtension : BeforeTestExecutionCallback, AfterTestExecutionCallback,
    ParameterResolver {

    private val namespace = ExtensionContext.Namespace.create(javaClass)

    override fun supportsParameter(parameterContext: ParameterContext, extensionContext: ExtensionContext): Boolean {
        return LogRecord::class.java == parameterContext.parameter.type
    }

    override fun resolveParameter(parameterContext: ParameterContext, extensionContext: ExtensionContext): Any {
        return extensionContext.logRecord
    }

    override fun beforeTestExecution(context: ExtensionContext) {
        val annotation = context.requiredTestMethod.getAnnotation(RecordLoggers::class.java)
            ?: error("no @RecordLoggers annotation found on test method!")
        val fromClasses = annotation.value.map { LogManager.getLogger(it.java) }
        val fromNames = annotation.names.map { LogManager.getLogger(it) }

        val logRecord = Log4jLogRecord()
        context.logRecord = logRecord

        val recorders = (fromClasses + fromNames)
            .distinct()
            .map { it as Logger }
            .map { Log4jLogRecorder(it, logRecord) }
            .onEach { it.start() }
        context.recorders = recorders
    }

    override fun afterTestExecution(context: ExtensionContext) {
        context.recorders.forEach { it.stop() }
    }

    private var ExtensionContext.recorders: List<Log4jLogRecorder>
        get() = store.get("log-recorders") as List<Log4jLogRecorder>
        set(value) = store.put("log-recorders", value)

    private var ExtensionContext.logRecord: Log4jLogRecord
        get() = store.get("log-record", Log4jLogRecord::class.java)
        set(value) = store.put("log-record", value)

    private val ExtensionContext.store: ExtensionContext.Store
        get() = getStore(namespace)

}