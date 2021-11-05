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
package info.novatec.testit.logrecorder.assertion.matchers.message

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class RegexMessageMatcherTests {

    @Test
    fun `message matching the RegEx matches`() {
        assertThat(matcherFor("Foo.+").matches("Foo bar XUR")).isTrue()
    }

    @Test
    fun `matching is done case sensitive`() {
        assertThat(matcherFor("foo.+").matches("Foo bar XUR")).isFalse()
    }

    @Test
    fun `message not matching the RegEx does not match`() {
        assertThat(matcherFor("Bar.+").matches("Foo bar XUR")).isFalse()
    }

    @Test
    fun `has an expressive toString value`() {
        assertThat(matcherFor("foo.+").toString()).isEqualTo("""matches ["foo.+"]""")
    }

    fun matcherFor(regex: String) = RegexMessageMatcher(regex)

}
