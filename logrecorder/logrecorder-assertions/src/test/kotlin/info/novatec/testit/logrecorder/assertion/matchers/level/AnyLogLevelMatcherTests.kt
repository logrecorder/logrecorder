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
package info.novatec.testit.logrecorder.assertion.matchers.level

import info.novatec.testit.logrecorder.api.LogLevel
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource

internal class AnyLogLevelMatcherTests {

    @ParameterizedTest
    @EnumSource(LogLevel::class)
    fun `any log level matches`(level: LogLevel) {
        assertThat(AnyLogLevelMatcher().matches(level)).isTrue()
    }

    @Test
    fun `has an expressive toString value`() {
        assertThat(AnyLogLevelMatcher().toString()).isEqualTo("ANY")
    }

}
