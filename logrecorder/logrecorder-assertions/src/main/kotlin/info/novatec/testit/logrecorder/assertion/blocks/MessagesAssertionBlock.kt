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
package info.novatec.testit.logrecorder.assertion.blocks

import info.novatec.testit.logrecorder.api.LogLevel
import info.novatec.testit.logrecorder.api.LogLevel.*
import info.novatec.testit.logrecorder.assertion.DslContext
import info.novatec.testit.logrecorder.assertion.matchers.LogLevelMatcher
import info.novatec.testit.logrecorder.assertion.matchers.MessageMatcher
import info.novatec.testit.logrecorder.assertion.matchers.level.AnyLogLevelMatcher
import info.novatec.testit.logrecorder.assertion.matchers.level.EqualLogLevelMatcher
import info.novatec.testit.logrecorder.assertion.matchers.message.*

@DslContext
interface MessagesAssertionBlock : AssertionBlock {

    fun addExpectation(logLevelMatcher: LogLevelMatcher, messageMatchers: List<MessageMatcher>)

    // message expectation factories

    /**
     * Define the expectation of a [TRACE] message equal to the provided `message`.
     */
    fun trace(message: String) = trace(equalTo(message))

    /**
     * Define the expectation of a [TRACE] message that complies with _all_ specified [MessageMatcher].
     *
     * Will match any [TRACE] if not at least one [MessageMatcher] is specified.
     */
    fun trace(vararg messageMatchers: MessageMatcher) = addExpectation(equalTo(TRACE), messageMatchers.toList())

    /**
     * Define the expectation of a [DEBUG] message equal to the provided `message`.
     */
    fun debug(message: String) = debug(equalTo(message))

    /**
     * Define the expectation of a [DEBUG] message that complies with _all_ specified [MessageMatcher].
     *
     * Will match any [DEBUG] if not at least one [MessageMatcher] is specified.
     */
    fun debug(vararg messageMatchers: MessageMatcher) = addExpectation(equalTo(DEBUG), messageMatchers.toList())

    /**
     * Define the expectation of a [INFO] message equal to the provided `message`.
     */
    fun info(message: String) = info(equalTo(message))

    /**
     * Define the expectation of a [INFO] message that complies with _all_ specified [MessageMatcher].
     *
     * Will match any [INFO] if not at least one [MessageMatcher] is specified.
     */
    fun info(vararg messageMatchers: MessageMatcher) = addExpectation(equalTo(INFO), messageMatchers.toList())

    /**
     * Define the expectation of a [WARN] message equal to the provided `message`.
     */
    fun warn(message: String) = warn(equalTo(message))

    /**
     * Define the expectation of a [WARN] message that complies with _all_ specified [MessageMatcher].
     *
     * Will match any [WARN] if not at least one [MessageMatcher] is specified.
     */
    fun warn(vararg messageMatchers: MessageMatcher) = addExpectation(equalTo(WARN), messageMatchers.toList())

    /**
     * Define the expectation of a [ERROR] message equal to the provided `message`.
     */
    fun error(message: String) = error(equalTo(message))

    /**
     * Define the expectation of a [ERROR] message that complies with _all_ specified [MessageMatcher].
     *
     * Will match any [WARN] if not at least one [MessageMatcher] is specified.
     */
    fun error(vararg messageMatchers: MessageMatcher) = addExpectation(equalTo(ERROR), messageMatchers.toList())

    /**
     * Define the expectation of a message, with any log level, and a value equal to the provided `message`.
     */
    fun any(message: String) = any(equalTo(message))

    /**
     * Define the expectation of a message, with any log level, that complies with _all_ specified [MessageMatcher].
     *
     * Will match any message if not at least one [MessageMatcher] is specified.
     */
    fun any(vararg messageMatchers: MessageMatcher) = addExpectation(anyLogLevel(), messageMatchers.toList())

    /**
     * This matcher can be used to skip log messages, that are not of any interest.
     * It will match any massage with any log level.
     */
    fun anything() = any()

    // message value matcher factories

    /**
     * Will match if the actual message is equal to the expected message.
     *
     * @see EqualMessageMatcher
     */
    fun equalTo(message: String): MessageMatcher = EqualMessageMatcher(message)

    /**
     * Will match if the actual message matches the provided regular expression.
     *
     * @see RegexMessageMatcher
     */
    fun matches(regex: String): MessageMatcher = RegexMessageMatcher(regex)

    /**
     * Will match if the actual message contains all the provided parts in _any order_.
     *
     * @see ContainsMessageMatcher
     */
    fun contains(vararg parts: String): MessageMatcher = ContainsMessageMatcher(parts.toList())

    /**
     * Will match if the actual message contains all the provided parts in that _exact order_.
     *
     * @see ContainsInOrderMessageMatcher
     */
    fun containsInOrder(vararg parts: String): MessageMatcher = ContainsInOrderMessageMatcher(parts.toList())

    /**
     * Will match if the actual message starts with the provided _prefix_.
     *
     * @see StartsWithMessageMatcher
     */
    fun startsWith(prefix: String): MessageMatcher = StartsWithMessageMatcher(prefix)

    /**
     * Will match if the actual message ends with the provided _suffix_.
     *
     * @see EndsWithMessageMatcher
     */
    fun endsWith(suffix: String): MessageMatcher = EndsWithMessageMatcher(suffix)

    // log level matcher factories

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
