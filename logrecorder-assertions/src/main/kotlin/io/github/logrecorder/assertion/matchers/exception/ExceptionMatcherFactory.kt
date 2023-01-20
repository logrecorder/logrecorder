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

package io.github.logrecorder.assertion.matchers.exception

import io.github.logrecorder.assertion.matchers.ExceptionMatcher

interface ExceptionMatcherFactory {

    /**
     * Will match if the actual exception is equal to the expected exception.
     *
     * This returns a list and is intended to be used as a DSL element where only one condition is declared.
     * (no need to wrap it in `listOf(equalTo(..))`)
     *
     * @see EqualExceptionMatcher
     */
    fun isEqualTo(exception: Throwable): List<ExceptionMatcher> = listOf(equalTo(exception))

    /**
     * Will match if the actual exception is equal to the expected exception.
     *
     * @see EqualExceptionMatcher
     */
    fun equalTo(exception: Throwable): ExceptionMatcher = EqualExceptionMatcher(exception)

    /**
     * Will match if there is no actual exception
     *
     * This returns a list and is intended to be used as a DSL element where only one condition is declared.
     * (no need to wrap it in `listOf(noException())`)
     *
     * @see NoExceptionMatcher
     */
    fun hasNoException(): List<ExceptionMatcher> = listOf(noException())

    /**
     * Will match if there is no actual exception
     *
     * @see NoExceptionMatcher
     */
    fun noException(): ExceptionMatcher = NoExceptionMatcher()

}
