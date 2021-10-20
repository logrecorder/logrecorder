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
package info.novatec.testit.logrecorder.log4j

import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.apache.logging.log4j.core.LoggerContext
import org.apache.logging.log4j.core.config.AppenderRef
import org.apache.logging.log4j.core.config.LoggerConfig
import org.apache.logging.log4j.core.layout.PatternLayout

internal class Log4jLogRecorder(
    logger: Logger,
    logRecord: Log4jLogRecord
) {
    private val loggerName = logger.name
    private val originalLoggerLevel = logger.level

    private val loggerContext = LogManager.getContext(false) as LoggerContext
    private val configuration = loggerContext.configuration
    private var newCreatedLogger = false

    private val appenderName = "TemporaryLogAppender-${System.nanoTime()}"
    private val appender = CallbackAppender(appenderName, null, PatternLayout.createDefaultLayout(), logRecord::record)
    private val appenderRef = AppenderRef.createAppenderRef(appenderName, null, null)
    private val appenderRefs = arrayOf(appenderRef)


    fun start() {
        appender.start()
        configuration.addAppender(appender)
        val loggerConfig = getLoggerConfig()
        if (!loggerConfig.name.equals(loggerName)) {
            val newLoggerConfig = LoggerConfig.createLogger(
                false,
                Level.ALL,
                loggerName,
                "true",
                appenderRefs,
                null,
                configuration,
                null
            )
            newLoggerConfig.addAppender(appender, null, null)
            configuration.addLogger(loggerName, newLoggerConfig)
            newCreatedLogger = true
        } else {
            loggerConfig.addAppender(appender, null, null)
            loggerConfig.level = Level.ALL
        }
        updateLoggerContext()
    }

    fun stop() {
        appender.stop()
        configuration.appenders.remove(appenderName)
        if (newCreatedLogger) {
            configuration.removeLogger(loggerName)
        } else {
            val loggerConfig = getLoggerConfig()
            loggerConfig.removeAppender(appender.name)
            loggerConfig.level = originalLoggerLevel
        }
        updateLoggerContext()
    }

    private fun getLoggerConfig(): LoggerConfig = configuration.getLoggerConfig(loggerName)

    private fun updateLoggerContext() {
        loggerContext.updateLoggers()
    }

}
