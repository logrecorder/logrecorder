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

package io.github.logrecorder.assertion.matchers.message

import io.github.logrecorder.assertion.matchers.MessageMatcher

interface MessageMatcherFactory {

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
     * Will match if the actual message does not contain any the provided parts.
     *
     * @see DoesNotContainMessageMatcher
     */
    fun doesNotContain(vararg parts: String): MessageMatcher = DoesNotContainMessageMatcher(parts.toList())

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

}
