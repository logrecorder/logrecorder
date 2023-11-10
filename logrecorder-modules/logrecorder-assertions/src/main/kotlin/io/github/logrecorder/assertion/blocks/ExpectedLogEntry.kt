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
package io.github.logrecorder.assertion.blocks

import io.github.logrecorder.api.LogEntry
import io.github.logrecorder.api.LogLevel
import io.github.logrecorder.assertion.matchers.ExceptionMatcher
import io.github.logrecorder.assertion.matchers.LogLevelMatcher
import io.github.logrecorder.assertion.matchers.MessageMatcher
import io.github.logrecorder.assertion.matchers.PropertyMatcher

class ExpectedLogEntry(
    val logLevelMatcher: LogLevelMatcher,
    val messageMatchers: List<MessageMatcher> = emptyList(),
    val propertyMatchers: List<PropertyMatcher> = emptyList(),
    val exceptionMatchers: List<ExceptionMatcher> = emptyList()
) {

    infix fun matches(actual: LogEntry): Boolean =
        matches(actual.level) && matches(actual.message) && matches(actual.properties) && matches(actual.throwable)

    infix fun matches(actual: LogLevel): Boolean =
        logLevelMatcher matches actual

    infix fun matches(actual: String): Boolean =
        messageMatchers.all { it matches actual }

    infix fun matches(actual: Map<String, String>): Boolean =
        propertyMatchers.all { it matches actual }

    infix fun matches(actual: Throwable?): Boolean =
        exceptionMatchers.all { it matches actual }

}
