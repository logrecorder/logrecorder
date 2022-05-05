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

import io.github.logrecorder.api.LogRecord
import io.github.logrecorder.assertion.blocks.AssertionBlock
import io.github.logrecorder.assertion.blocks.Contains
import io.github.logrecorder.assertion.blocks.ContainsExactly
import io.github.logrecorder.assertion.blocks.ContainsInOrder
import io.github.logrecorder.assertion.blocks.ContainsOnly
import io.github.logrecorder.assertion.blocks.IsEmpty
import io.github.logrecorder.assertion.blocks.MessagesAssertionBlock

@DslContext
class LogRecordAssertion private constructor(
    private val logRecord: LogRecord
) {

    @Deprecated("replaced with assertBlock(..)", ReplaceWith("assertBlock(block)"))
    fun addAssertionBlock(block: AssertionBlock) {
        assertBlock(block)
    }

    fun assertBlock(block: AssertionBlock): LogRecordAssertion {
        block.check(logRecord)
        return this
    }

    companion object {

        @JvmStatic
        fun assertThatLog(logRecord: LogRecord, block: LogRecordAssertion.() -> Unit) {
            assertThat(logRecord, block)
        }

        @JvmStatic
        fun assertThat(logRecord: LogRecord, block: LogRecordAssertion.() -> Unit) {
            LogRecordAssertion(logRecord).apply(block)
        }

        @JvmStatic
        fun assertThatLog(logRecord: LogRecord) = LogRecordAssertion(logRecord)

        @JvmStatic
        fun assertThat(logRecord: LogRecord) = LogRecordAssertion(logRecord)
    }
}

/**
 * Define an [IsEmpty] assertion block.
 */
fun LogRecordAssertion.isEmpty() =
    assertBlock(IsEmpty())

/**
 * Define a [Contains] assertion block.
 */
infix fun LogRecordAssertion.contains(block: MessagesAssertionBlock.() -> Unit) =
    assertBlock(Contains().apply(block))

/**
 * Define a [ContainsOnly] assertion block.
 */
infix fun LogRecordAssertion.containsOnly(block: MessagesAssertionBlock.() -> Unit) =
    assertBlock(ContainsOnly().apply(block))

/**
 * Define a [ContainsExactly] assertion block.
 */
infix fun LogRecordAssertion.containsExactly(block: MessagesAssertionBlock.() -> Unit) =
    assertBlock(ContainsExactly().apply(block))

/**
 * Define a [ContainsInOrder] assertion block.
 */
infix fun LogRecordAssertion.containsInOrder(block: MessagesAssertionBlock.() -> Unit) =
    assertBlock(ContainsInOrder().apply(block))
