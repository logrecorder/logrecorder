/*
 * Copyright 2017-2023 the original author or authors.
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

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import kotlin.reflect.KClass

internal class InstanceOfExceptionMatcherTests {

    @Test
    fun `matches for same exception type`() {
        assertMatches(
            type = TestException::class,
            actual = TestException(),
            matches = true
        )
    }

    @Test
    fun `matches for parent exception type`() {
        assertMatches(
            type = RuntimeException::class,
            actual = TestException(),
            matches = true
        )
    }

    @Test
    fun `does not match for different exception type`() {
        assertMatches(
            type = TestException::class,
            actual = RuntimeException(),
            matches = false
        )
    }

    @Test
    fun `does not match if actual is null`() {
        assertMatches(
            type = TestException::class,
            actual = null,
            matches = false
        )
    }

    @Test
    fun `has an expressive toString value`() {
        assertThat(matcherFor(RuntimeException::class).toString())
            .isEqualTo("""instance of java.lang.RuntimeException""")
    }

    fun assertMatches(type: KClass<*>, actual: Throwable?, matches: Boolean) {
        assertThat(matcherFor(type).matches(actual)).isEqualTo(matches)
    }

    fun matcherFor(exceptionType: KClass<*>) = InstanceOfExceptionMatcher(exceptionType)

    class TestException : RuntimeException()
}
