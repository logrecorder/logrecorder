/*
 * Copyright 2017-2022 the original author or authors.
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

package io.github.logrecorder.assertion.matchers.level

import io.github.logrecorder.api.LogLevel
import io.github.logrecorder.assertion.matchers.LogLevelMatcher

interface LogLevelMatcherFactory {

    /**
     * Will match if the actual log level is equal to the expected level.
     *
     * @see EqualLogLevelMatcher
     */
    fun equalTo(logLevel: LogLevel): LogLevelMatcher = EqualLogLevelMatcher(logLevel)

    /**
     * Will match for all log levels.
     *
     * @see AnyLogLevelMatcher
     */
    fun anyLogLevel(): LogLevelMatcher = AnyLogLevelMatcher()

}
