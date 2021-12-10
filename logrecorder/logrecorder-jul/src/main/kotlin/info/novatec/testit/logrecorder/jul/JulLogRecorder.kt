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
package info.novatec.testit.logrecorder.jul

import java.util.logging.Level
import java.util.logging.Logger

internal class JulLogRecorder(
    private val logger: Logger,
    private val julLogRecord: JulLogRecord
) {

    private val originalLogLevel = logger.level

    private val callbackHandler = CallbackHandler(julLogRecord::record)

    fun start() {
        logger.level = Level.FINER
        logger.addHandler(callbackHandler)
    }

    fun stop() {
        logger.level = originalLogLevel
        logger.removeHandler(callbackHandler)
    }
}