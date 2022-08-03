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

import io.github.logrecorder.api.LogLevel.DEBUG
import io.github.logrecorder.api.LogLevel.ERROR
import io.github.logrecorder.api.LogLevel.INFO
import io.github.logrecorder.api.LogLevel.TRACE
import io.github.logrecorder.api.LogLevel.WARN
import io.github.logrecorder.assertion.DslContext
import io.github.logrecorder.assertion.matchers.LogLevelMatcher
import io.github.logrecorder.assertion.matchers.MessageMatcher
import io.github.logrecorder.assertion.matchers.PropertyMatcher
import io.github.logrecorder.assertion.matchers.level.LogLevelMatcherFactory
import io.github.logrecorder.assertion.matchers.message.MessageMatcherFactory
import io.github.logrecorder.assertion.matchers.properties.PropertyMatcherFactory

@DslContext
interface MessagesAssertionBlock : AssertionBlock,
    MessageMatcherFactory, PropertyMatcherFactory, LogLevelMatcherFactory {

    fun addExpectation(
        logLevelMatcher: LogLevelMatcher,
        messageMatchers: List<MessageMatcher> = emptyList(),
        propertyMatchers: List<PropertyMatcher> = emptyList()
    )

    // TRACE

    /**
     * Define the expectation of a [TRACE] message equal to the provided `message`
     * and properties matching the given [PropertyMatcher].
     */
    fun trace(message: String, properties: List<PropertyMatcher> = emptyList()) =
        trace(message = listOf(equalTo(message)), properties = properties)

    /**
     * Define the expectation of a [TRACE] message that complies with _all_ specified [MessageMatcher].
     *
     * Will match any [TRACE] if not at least one [MessageMatcher] is specified.
     */
    fun trace(vararg messageMatchers: MessageMatcher) = trace(message = messageMatchers.toList())

    /**
     * Define the expectation of a [TRACE] message that complies with _all_ specified [MessageMatcher]
     * and [PropertyMatcher].
     *
     * Will match any [TRACE] if not at least one [MessageMatcher] or [PropertyMatcher] is specified.
     */
    fun trace(message: List<MessageMatcher> = emptyList(), properties: List<PropertyMatcher> = emptyList()) =
        addExpectation(equalTo(TRACE), message, properties)

    // DEBUG

    /**
     * Define the expectation of a [DEBUG] message equal to the provided `message`
     * and properties matching the given [PropertyMatcher].
     */
    fun debug(message: String, properties: List<PropertyMatcher> = emptyList()) =
        debug(message = listOf(equalTo(message)), properties = properties)

    /**
     * Define the expectation of a [DEBUG] message that complies with _all_ specified [MessageMatcher].
     *
     * Will match any [DEBUG] if not at least one [MessageMatcher] is specified.
     */
    fun debug(vararg messageMatchers: MessageMatcher) = debug(message = messageMatchers.toList())

    /**
     * Define the expectation of a [DEBUG] message that complies with _all_ specified [MessageMatcher]
     * and [PropertyMatcher].
     *
     * Will match any [DEBUG] if not at least one [MessageMatcher] or [PropertyMatcher] is specified.
     */
    fun debug(message: List<MessageMatcher> = emptyList(), properties: List<PropertyMatcher> = emptyList()) =
        addExpectation(equalTo(DEBUG), message, properties)

    // INFO

    /**
     * Define the expectation of a [INFO] message equal to the provided `message`
     * and properties matching the given [PropertyMatcher].
     */
    fun info(message: String, properties: List<PropertyMatcher> = emptyList()) =
        info(message = listOf(equalTo(message)), properties = properties)

    /**
     * Define the expectation of a [INFO] message that complies with _all_ specified [MessageMatcher].
     *
     * Will match any [INFO] if not at least one [MessageMatcher] is specified.
     */
    fun info(vararg messageMatchers: MessageMatcher) = info(message = messageMatchers.toList())

    /**
     * Define the expectation of a [INFO] message that complies with _all_ specified [MessageMatcher]
     * and [PropertyMatcher].
     *
     * Will match any [INFO] if not at least one [MessageMatcher] or [PropertyMatcher] is specified.
     */
    fun info(message: List<MessageMatcher> = emptyList(), properties: List<PropertyMatcher> = emptyList()) =
        addExpectation(equalTo(INFO), message, properties)

    // WARN

    /**
     * Define the expectation of a [WARN] message equal to the provided `message`
     * and properties matching the given [PropertyMatcher].
     */
    fun warn(message: String, properties: List<PropertyMatcher> = emptyList()) =
        warn(message = listOf(equalTo(message)), properties = properties)

    /**
     * Define the expectation of a [WARN] message that complies with _all_ specified [MessageMatcher].
     *
     * Will match any [WARN] if not at least one [MessageMatcher] is specified.
     */
    fun warn(vararg messageMatchers: MessageMatcher) = warn(message = messageMatchers.toList())

    /**
     * Define the expectation of a [WARN] message that complies with _all_ specified [MessageMatcher]
     * and [PropertyMatcher].
     *
     * Will match any [WARN] if not at least one [MessageMatcher] or [PropertyMatcher] is specified.
     */
    fun warn(message: List<MessageMatcher> = emptyList(), properties: List<PropertyMatcher> = emptyList()) =
        addExpectation(equalTo(WARN), message, properties)

    // ERROR

    /**
     * Define the expectation of a [ERROR] message equal to the provided `message`
     * and properties matching the given [PropertyMatcher].
     */
    fun error(message: String, properties: List<PropertyMatcher> = emptyList()) =
        error(message = listOf(equalTo(message)), properties = properties)

    /**
     * Define the expectation of a [ERROR] message that complies with _all_ specified [MessageMatcher].
     *
     * Will match any [ERROR] if not at least one [MessageMatcher] is specified.
     */
    fun error(vararg messageMatchers: MessageMatcher) = error(message = messageMatchers.toList())

    /**
     * Define the expectation of a [ERROR] message that complies with _all_ specified [MessageMatcher]
     * and [PropertyMatcher].
     *
     * Will match any [ERROR] if not at least one [MessageMatcher] or [PropertyMatcher] is specified.
     */
    fun error(message: List<MessageMatcher> = emptyList(), properties: List<PropertyMatcher> = emptyList()) =
        addExpectation(equalTo(ERROR), message, properties)

    // ANY

    /**
     * Define the expectation of a message, with any log level, a value equal to the provided `message`
     * and properties matching the given [PropertyMatcher].
     */
    fun any(message: String, properties: List<PropertyMatcher> = emptyList()) =
        any(message = listOf(equalTo(message)), properties = properties)

    /**
     * Define the expectation of a message, with any log level, that complies with _all_ specified [MessageMatcher].
     *
     * Will match any message if not at least one [MessageMatcher] is specified.
     */
    fun any(vararg messageMatchers: MessageMatcher) = any(message = messageMatchers.toList())

    /**
     * Define the expectation of a message, with any log level, that complies with _all_ specified [MessageMatcher]
     * and [PropertyMatcher].
     *
     * Will match any message if not at least one [MessageMatcher] or [PropertyMatcher] is specified.
     */
    fun any(message: List<MessageMatcher> = emptyList(), properties: List<PropertyMatcher> = emptyList()) =
        addExpectation(anyLogLevel(), message, properties)

    /**
     * This matcher can be used to skip log messages, that are not of any interest.
     * It will match any massage with any log level.
     */
    fun anything() = any()

}
