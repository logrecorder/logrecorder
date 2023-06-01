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
package io.github.logrecorder.assertion.matchers.message

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class DoesNotContainMessageMatcherTests {

    @Test
    fun `message containing none of the supplied parts matches`() {
        assertThat(matcherFor("Foo", "XUR").matches("Lorem Ipsum")).isTrue()
    }

    @Test
    fun `message containing any of the supplied parts does not match`() {
        assertThat(matcherFor("Foo", "XUR").matches("Foo bar XUR")).isFalse()
        assertThat(matcherFor("Foo", "XUR").matches("bar XUR")).isFalse()
        assertThat(matcherFor("Foo", "XUR").matches("Foo bar XUR")).isFalse()
    }

    @Test
    fun `matching is done case sensitive`() {
        assertThat(matcherFor("foo").matches("Foo bar XUR")).isTrue()
        assertThat(matcherFor("Foo").matches("Foo bar XUR")).isFalse()
    }

    @Test
    fun `has an expressive toString value`() {
        assertThat(matcherFor("foo", "bar").toString()).isEqualTo("""does not contain any of ["foo", "bar"]""")
    }

    fun matcherFor(vararg parts: String) = DoesNotContainMessageMatcher(parts.toList())

}
