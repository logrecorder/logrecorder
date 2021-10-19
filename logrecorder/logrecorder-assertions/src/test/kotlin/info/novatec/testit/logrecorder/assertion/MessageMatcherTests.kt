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
package info.novatec.testit.logrecorder.assertion

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class MessageMatcherTests {

    val message = "Foo bar XUR"

    @Nested
    inner class EqualMessageMatcherTests {

        @Test
        fun `equal message matches`() {
            assertThat(EqualMessageMatcher("Foo bar XUR").matches(message)).isTrue()
        }

        @Test
        fun `matching is done case sensitive`() {
            assertThat(EqualMessageMatcher("foo bar xur").matches(message)).isFalse()
        }

        @Test
        fun `unequal message does not match`() {
            assertThat(EqualMessageMatcher("something else").matches(message)).isFalse()
        }

    }

    @Nested
    inner class RegexMessageMatcherTests {

        @Test
        fun `message matching the RegEx matches`() {
            assertThat(RegexMessageMatcher("Foo.+").matches(message)).isTrue()
        }

        @Test
        fun `matching is done case sensitive`() {
            assertThat(RegexMessageMatcher("foo.+").matches(message)).isFalse()
        }

        @Test
        fun `message not matching the RegEx does not match`() {
            assertThat(RegexMessageMatcher("Bar.+").matches(message)).isFalse()
        }

    }

    @Nested
    inner class ContainsMessageMatcherTests {

        @Test
        fun `message containing all supplied parts matches`() {
            assertThat(ContainsMessageMatcher(listOf("Foo", "XUR")).matches(message)).isTrue()
        }

        @Test
        fun `message containing all supplied parts in any order matches`() {
            assertThat(ContainsMessageMatcher(listOf("XUR", "Foo")).matches(message)).isTrue()
        }

        @Test
        fun `matching is done case sensitive`() {
            assertThat(ContainsMessageMatcher(listOf("foo", "xur")).matches(message)).isFalse()
        }

        @Test
        fun `message containing non of the parts does not match`() {
            assertThat(ContainsMessageMatcher(listOf("message")).matches(message)).isFalse()
        }

        @Test
        fun `message containing only some of the parts does not match`() {
            assertThat(ContainsMessageMatcher(listOf("Foo", "message")).matches(message)).isFalse()
        }

    }

    @Nested
    inner class ContainsInOrderMessageMatcherTests {

        @Test
        fun `message containing all supplied parts in order matches`() {
            assertThat(ContainsInOrderMessageMatcher(listOf("Foo", "XUR")).matches(message)).isTrue()
        }

        @Test
        fun `message containing all supplied parts but not in order does not match`() {
            assertThat(ContainsInOrderMessageMatcher(listOf("XUR", "Foo")).matches(message)).isFalse()
        }

        @Test
        fun `matching is done case sensitive`() {
            assertThat(ContainsInOrderMessageMatcher(listOf("foo", "xur")).matches(message)).isFalse()
        }

        @Test
        fun `message containing non of the parts does not match`() {
            assertThat(ContainsInOrderMessageMatcher(listOf("message")).matches(message)).isFalse()
        }

        @Test
        fun `message containing only some of the parts does not match`() {
            assertThat(ContainsInOrderMessageMatcher(listOf("Foo", "message")).matches(message)).isFalse()
        }

    }

    @Nested
    inner class StartsWithMessageMatcherTests {

        @Test
        fun `message starting with prefix matches`() {
            assertThat(StartsWithMessageMatcher("Foo ").matches(message)).isTrue()
        }

        @Test
        fun `message not starting with prefix doe not match`() {
            assertThat(StartsWithMessageMatcher("message").matches(message)).isFalse()
        }

        @Test
        fun `matching is done case sensitive`() {
            assertThat(StartsWithMessageMatcher("foo ").matches(message)).isFalse()
        }

    }

    @Nested
    inner class EndsWithMessageMatcherTests {

        @Test
        fun `message starting with prefix matches`() {
            assertThat(EndsWithMessageMatcher(" XUR").matches(message)).isTrue()
        }

        @Test
        fun `message not starting with prefix doe not match`() {
            assertThat(EndsWithMessageMatcher("message").matches(message)).isFalse()
        }

        @Test
        fun `matching is done case sensitive`() {
            assertThat(EndsWithMessageMatcher(" xur").matches(message)).isFalse()
        }

    }
}
