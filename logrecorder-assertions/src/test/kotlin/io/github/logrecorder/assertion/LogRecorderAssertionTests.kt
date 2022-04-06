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
package io.github.logrecorder.assertion

import io.github.logrecorder.api.LogLevel.DEBUG
import io.github.logrecorder.api.LogLevel.INFO
import io.github.logrecorder.assertion.LogRecordAssertion.Companion.assertThat
import org.junit.jupiter.api.Test

class LogRecorderAssertionTests {

    val log = logRecord(
        logEntry(INFO, "message #1"),
        logEntry(DEBUG, "message #2")
    )

    @Test
    fun `example - block based DSL usage`() {
        assertThat(log) {
            containsExactly {
                info("message #1")
                debug(startsWith("message"), endsWith("#2"))
            }
            contains {
                info(matches("[a-z]+ #1"))
            }
        }
    }

    @Test
    fun `example - fluent usage`() {
        assertThat(log)
            .containsExactly {
                info("message #1")
                debug(startsWith("message"), endsWith("#2"))
            }
            .contains {
                info(matches("[a-z]+ #1"))
            }
    }

    @Test
    fun `example - infix usage`() {
        assertThat(log) contains { info("message #1") }
    }
}
