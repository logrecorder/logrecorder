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

internal class EqualExceptionMatcherTests {

    @Test
    fun `matches if it is the same exception`() {
        val exception = RuntimeException()

        assertMatches(expected = exception, actual = exception, matches = true)
    }

    @Test
    fun `does not match if it is not the same exception`() {
        val exception1 = RuntimeException()
        val exception2 = RuntimeException()

        assertMatches(expected = exception1, actual = exception2, matches = false)
    }

    @Test
    fun `does not match if actual is null`() {
        assertMatches(expected = RuntimeException(), actual = null, matches = false)
    }

    fun assertMatches(expected: Throwable, actual: Throwable?, matches: Boolean) {
        assertThat(matcherFor(expected).matches(actual)).isEqualTo(matches)
    }

    @Test
    fun `has an expressive toString value - without message`() {
        assertThat(matcherFor(RuntimeException()).toString()).isEqualTo("""equal to RuntimeException()""")
    }

    @Test
    fun `has an expressive toString value - with message`() {
        assertThat(matcherFor(RuntimeException("oops")).toString()).isEqualTo("""equal to RuntimeException("oops")""")
    }

    @Test
    fun `has an expressive toString value - anonymous`() {
        val exception = object : RuntimeException("oops") {}
        assertThat(matcherFor(exception).toString()).isEqualTo("""equal to unknown-exception("oops")""")
    }

    fun matcherFor(exception: Throwable) = EqualExceptionMatcher(exception)

}
