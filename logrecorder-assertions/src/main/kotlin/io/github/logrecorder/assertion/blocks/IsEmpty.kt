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
package io.github.logrecorder.assertion.blocks

import io.github.logrecorder.api.LogRecord

/**
 * [AssertionBlock] for the different assertion DSL styles.
 *
 * This assertion block requires the log to be empty.
 *
 * **Examples:**
 * ```
 * // AssertJ style
 * assertThat(log).isEmpty()
 *
 * // Kotest style
 * log shouldBe empty
 * ```
 *
 * @since 1.5.0
 */
class IsEmpty : AssertionBlock {

    override fun check(logRecord: LogRecord) {
        if (logRecord.entries.isNotEmpty()) {
            val message = logRecord.entries
                .joinToString(separator = "\n", prefix = "Expected log to be empty but there were messages:\n") {
                    "${it.level} | ${it.message}"
                }
            throw AssertionError(message)
        }
    }

}
