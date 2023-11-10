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
package io.github.logrecorder.logback

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Logger
import io.github.logrecorder.common.LogRecorder

class LogbackLogRecorder(
    private val logger: Logger,
    logRecord: LogbackLogRecord
) : LogRecorder {

    private val originalLevel = logger.effectiveLevel
    private val appender = CallbackAppender(name(), logRecord::record)

    override fun start() {
        logger.level = Level.ALL
        logger.addAppender(appender)
    }

    override fun stop() {
        logger.detachAppender(appender)
        logger.level = originalLevel
    }

    private fun name(): String = "temporary log appender #${System.nanoTime()}"

}
