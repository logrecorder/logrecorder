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
package io.github.logrecorder.logback.junit5

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Logger
import io.github.logrecorder.api.LogRecord
import io.github.logrecorder.common.junit5.AbstractLogRecorderExtension
import io.github.logrecorder.logback.LogbackLogRecord
import io.github.logrecorder.logback.LogbackLogRecorder
import org.slf4j.LoggerFactory
import java.lang.reflect.Method
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
 * @since 1.0
 */
internal class LogbackRecorderExtension : AbstractLogRecorderExtension<Logger, LogbackLogRecord>() {
    override val loggerFromKClass = { source: KClass<*> -> LoggerFactory.getLogger(source.java) as Logger }
    override val loggerFromName = { name: String -> LoggerFactory.getLogger(name) as Logger }

    override fun getLoggers(testMethod: Method): Set<Logger> {
        val annotation = testMethod.getAnnotation(RecordLoggers::class.java)
            ?: error("no @RecordLoggers annotation found on test method!")
        return getLoggers(annotation.value, annotation.names).toSet()
    }

    override fun createLogRecord() = LogbackLogRecord()
    override fun createLogRecorder(logger: Logger, logRecord: LogbackLogRecord) = LogbackLogRecorder(logger, logRecord)

}
