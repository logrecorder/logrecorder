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
package info.novatec.testit.logrecorder.assertion.blocks

import info.novatec.testit.logrecorder.api.LogLevel.DEBUG
import info.novatec.testit.logrecorder.api.LogLevel.INFO
import info.novatec.testit.logrecorder.assertion.LogRecordAssertion.Companion.assertThat
import info.novatec.testit.logrecorder.assertion.isEmpty
import info.novatec.testit.logrecorder.assertion.logEntry
import info.novatec.testit.logrecorder.assertion.logRecord
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class IsEmptyTests {

    @Test
    fun `reference check - empty`() {
        val emptyLog = logRecord()

        assertThat(emptyLog) { isEmpty() }
    }

    @Test
    fun `non empty record throws assertion error`() {
        val log = logRecord(
            logEntry(level = INFO, message = "message #1"),
            logEntry(level = DEBUG, message = "message #2")
        )

        val ex = assertThrows<AssertionError> {
            assertThat(log) { isEmpty() }
        }

        assertThat(ex).hasMessage(
            """
            Expected log to be empty but there were messages:
            INFO | message #1
            DEBUG | message #2
            """.trimIndent()
        )
    }

}
