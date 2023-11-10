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
import io.github.logrecorder.api.LogLevel.WARN
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class KotestStyleAssertionsTests {

    @Test
    fun `example shouldBe empty usage`() {
        val log = logRecord()

        log shouldBe empty
    }

    @Test
    fun `example shouldContain usage`() {
        val log = logRecord(
            logEntry(INFO, "message #1"),
            logEntry(DEBUG, "message #2")
        )

        log shouldContain {
            debug("message #2")
        }

        assertThrows<AssertionError> {
            log shouldContain {
                warn("message #3")
            }
        }
    }

    @Test
    fun `example shouldContainOnly usage`() {
        val log = logRecord(
            logEntry(INFO, "message #1"),
            logEntry(DEBUG, "message #2")
        )

        log shouldContainOnly {
            debug("message #2")
            info("message #1")
        }

        assertThrows<AssertionError> {
            log shouldContainOnly {
                info("message #1")
            }
        }
    }

    @Test
    fun `example shouldContainExactly usage`() {
        val log = logRecord(
            logEntry(INFO, "message #1"),
            logEntry(DEBUG, "message #2"),
            logEntry(WARN, "message #3")
        )

        log shouldContainExactly {
            info("message #1")
            debug("message #2")
            warn("message #3")
        }

        assertThrows<AssertionError> {
            log shouldContainExactly {
                info("message #1")
                debug("message #2")
            }
        }
    }

    @Test
    fun `example shouldContainInOrder usage`() {
        val log = logRecord(
            logEntry(INFO, "message #1"),
            logEntry(DEBUG, "message #2"),
            logEntry(WARN, "message #3")
        )

        log shouldContainInOrder {
            info("message #1")
            warn("message #3")
        }

        assertThrows<AssertionError> {
            log shouldContainInOrder {
                warn("message #3")
                info("message #1")
            }
        }
    }

}
