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
import io.github.logrecorder.assertion.matchers.ExceptionMatcher
import io.github.logrecorder.assertion.matchers.LogLevelMatcher
import io.github.logrecorder.assertion.matchers.MessageMatcher
import io.github.logrecorder.assertion.matchers.PropertyMatcher
import io.github.logrecorder.assertion.matchers.exception.ExceptionMatcherFactory
import io.github.logrecorder.assertion.matchers.level.LogLevelMatcherFactory
import io.github.logrecorder.assertion.matchers.message.MessageMatcherFactory
import io.github.logrecorder.assertion.matchers.properties.PropertyMatcherFactory

@DslContext
interface MessagesAssertionBlock : AssertionBlock,
    MessageMatcherFactory, PropertyMatcherFactory, LogLevelMatcherFactory, ExceptionMatcherFactory {

    fun addExpectation(
        logLevelMatcher: LogLevelMatcher,
        messageMatchers: List<MessageMatcher> = emptyList(),
        propertyMatchers: List<PropertyMatcher> = emptyList(),
        exceptionMatchers: List<ExceptionMatcher> = emptyList()
    )

    // TRACE

    /**
     * Define the expectation of a [TRACE] message equal to the provided `message`, that complies
     * with _all_ specified [PropertyMatcher] and [ExceptionMatcher].
     */
    fun trace(
        message: String,
        properties: List<PropertyMatcher> = emptyList(),
        exception: List<ExceptionMatcher> = emptyList()
    ) = trace(message = listOf(equalTo(message)), properties = properties, exception = exception)

    /**
     * Define the expectation of a [TRACE] message that complies with _all_ specified [MessageMatcher].
     *
     * Will match any [TRACE] if not at least one [MessageMatcher] is specified.
     */
    fun trace(vararg messageMatchers: MessageMatcher) = trace(message = messageMatchers.toList())

    /**
     * Define the expectation of a [TRACE] message that complies with _all_ specified [MessageMatcher],
     * [PropertyMatcher] and [ExceptionMatcher].
     *
     * Will match any [TRACE] if not at least one [MessageMatcher], [PropertyMatcher] or [ExceptionMatcher] is specified.
     */
    fun trace(
        message: List<MessageMatcher> = emptyList(),
        properties: List<PropertyMatcher> = emptyList(),
        exception: List<ExceptionMatcher> = emptyList()
    ) = addExpectation(equalTo(TRACE), message, properties, exception)

    // DEBUG

    /**
     * Define the expectation of a [DEBUG] message equal to the provided `message`, that complies
     * with _all_ specified [PropertyMatcher] and [ExceptionMatcher].
     */
    fun debug(
        message: String,
        properties: List<PropertyMatcher> = emptyList(),
        exception: List<ExceptionMatcher> = emptyList()
    ) = debug(message = listOf(equalTo(message)), properties = properties, exception = exception)

    /**
     * Define the expectation of a [DEBUG] message that complies with _all_ specified [MessageMatcher].
     *
     * Will match any [DEBUG] if not at least one [MessageMatcher] is specified.
     */
    fun debug(vararg messageMatchers: MessageMatcher) = debug(message = messageMatchers.toList())

    /**
     * Define the expectation of a [DEBUG] message that complies with _all_ specified [MessageMatcher],
     * [PropertyMatcher] and [ExceptionMatcher].
     *
     * Will match any [DEBUG] if not at least one [MessageMatcher], [PropertyMatcher] or [ExceptionMatcher] is specified.
     */
    fun debug(
        message: List<MessageMatcher> = emptyList(),
        properties: List<PropertyMatcher> = emptyList(),
        exception: List<ExceptionMatcher> = emptyList()
    ) = addExpectation(equalTo(DEBUG), message, properties, exception)

    // INFO

    /**
     * Define the expectation of a [INFO] message equal to the provided `message`, that complies
     * with _all_ specified [PropertyMatcher] and [ExceptionMatcher].
     */
    fun info(
        message: String,
        properties: List<PropertyMatcher> = emptyList(),
        exception: List<ExceptionMatcher> = emptyList()
    ) = info(message = listOf(equalTo(message)), properties = properties, exception = exception)

    /**
     * Define the expectation of a [INFO] message that complies with _all_ specified [MessageMatcher].
     *
     * Will match any [INFO] if not at least one [MessageMatcher] is specified.
     */
    fun info(vararg messageMatchers: MessageMatcher) = info(message = messageMatchers.toList())

    /**
     * Define the expectation of a [INFO] message that complies with _all_ specified [MessageMatcher],
     * [PropertyMatcher] and [ExceptionMatcher].
     *
     * Will match any [INFO] if not at least one [MessageMatcher], [PropertyMatcher] or [ExceptionMatcher] is specified.
     */
    fun info(
        message: List<MessageMatcher> = emptyList(),
        properties: List<PropertyMatcher> = emptyList(),
        exception: List<ExceptionMatcher> = emptyList()
    ) = addExpectation(equalTo(INFO), message, properties, exception)

    // WARN

    /**
     * Define the expectation of a [WARN] message equal to the provided `message`, that complies
     * with _all_ specified [PropertyMatcher] and [ExceptionMatcher].
     */
    fun warn(
        message: String,
        properties: List<PropertyMatcher> = emptyList(),
        exception: List<ExceptionMatcher> = emptyList()
    ) = warn(message = listOf(equalTo(message)), properties = properties, exception = exception)

    /**
     * Define the expectation of a [WARN] message that complies with _all_ specified [MessageMatcher].
     *
     * Will match any [WARN] if not at least one [MessageMatcher] is specified.
     */
    fun warn(vararg messageMatchers: MessageMatcher) = warn(message = messageMatchers.toList())

    /**
     * Define the expectation of a [WARN] message that complies with _all_ specified [MessageMatcher],
     * [PropertyMatcher] and [ExceptionMatcher].
     *
     * Will match any [WARN] if not at least one [MessageMatcher], [PropertyMatcher] or [ExceptionMatcher] is specified.
     */
    fun warn(
        message: List<MessageMatcher> = emptyList(),
        properties: List<PropertyMatcher> = emptyList(),
        exception: List<ExceptionMatcher> = emptyList()
    ) = addExpectation(equalTo(WARN), message, properties, exception)

    // ERROR

    /**
     * Define the expectation of a [ERROR] message equal to the provided `message`, that complies
     * with _all_ specified [PropertyMatcher] and [ExceptionMatcher].
     */
    fun error(
        message: String,
        properties: List<PropertyMatcher> = emptyList(),
        exception: List<ExceptionMatcher> = emptyList()
    ) = error(message = listOf(equalTo(message)), properties = properties, exception = exception)

    /**
     * Define the expectation of a [ERROR] message that complies with _all_ specified [MessageMatcher].
     *
     * Will match any [ERROR] if not at least one [MessageMatcher] is specified.
     */
    fun error(vararg messageMatchers: MessageMatcher) = error(message = messageMatchers.toList())

    /**
     * Define the expectation of a [ERROR] message that complies with _all_ specified [MessageMatcher],
     * [PropertyMatcher] and [ExceptionMatcher].
     *
     * Will match any [ERROR] if not at least one [MessageMatcher], [PropertyMatcher] or [ExceptionMatcher] is specified.
     */
    fun error(
        message: List<MessageMatcher> = emptyList(),
        properties: List<PropertyMatcher> = emptyList(),
        exception: List<ExceptionMatcher> = emptyList()
    ) = addExpectation(equalTo(ERROR), message, properties, exception)

    // ANY

    /**
     * Define the expectation of a message, with any log level, a value equal to the provided `message`,
     * that complies with _all_ specified [PropertyMatcher] and [ExceptionMatcher].
     */
    fun any(
        message: String,
        properties: List<PropertyMatcher> = emptyList(),
        exception: List<ExceptionMatcher> = emptyList()
    ) = any(message = listOf(equalTo(message)), properties = properties, exception = exception)

    /**
     * Define the expectation of a message, with any log level, that complies with _all_ specified [MessageMatcher].
     *
     * Will match any message if not at least one [MessageMatcher] is specified.
     */
    fun any(vararg messageMatchers: MessageMatcher) = any(message = messageMatchers.toList())

    /**
     * Define the expectation of a message, with any log level, that complies with _all_ specified [MessageMatcher],
     * [PropertyMatcher] and [ExceptionMatcher].
     *
     * Will match any message if not at least one [MessageMatcher], [PropertyMatcher] or [ExceptionMatcher] is specified.
     */
    fun any(
        message: List<MessageMatcher> = emptyList(),
        properties: List<PropertyMatcher> = emptyList(),
        exception: List<ExceptionMatcher> = emptyList()
    ) = addExpectation(anyLogLevel(), message, properties, exception)

    /**
     * This matcher can be used to skip log messages, that are not of any interest.
     * It will match any massage with any log level.
     */
    fun anything() = any()

}
