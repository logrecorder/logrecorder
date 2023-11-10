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

package io.github.logrecorder.assertion.matchers.properties

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class ContainsPropertyMatcherTests {

    @Test
    fun `matches if properties contains exact match`() {
        val matcher = matcherFor("key", "value")
        val properties = mapOf("key" to "value")
        assertThat(matcher.matches(properties)).isTrue()
    }

    @Test
    fun `does not match if properties do not contain key-value pair`() {
        val matcher = matcherFor("key", "value")
        val properties = mapOf("foo" to "bar")
        assertThat(matcher.matches(properties)).isFalse()
    }

    @Test
    fun `does not match if properties does not have same casing`() {
        val matcher = matcherFor("key", "value")
        val properties = mapOf("Key" to "Value")
        assertThat(matcher.matches(properties)).isFalse()
    }

    fun matcherFor(key: String, value: String) = ContainsPropertyMatcher(key, value)

}
