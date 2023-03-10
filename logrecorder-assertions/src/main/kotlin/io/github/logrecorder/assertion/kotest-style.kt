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

package io.github.logrecorder.assertion

import io.github.logrecorder.api.LogRecord
import io.github.logrecorder.assertion.blocks.Contains
import io.github.logrecorder.assertion.blocks.ContainsExactly
import io.github.logrecorder.assertion.blocks.ContainsInOrder
import io.github.logrecorder.assertion.blocks.ContainsOnly
import io.github.logrecorder.assertion.blocks.IsEmpty
import io.github.logrecorder.assertion.blocks.MessagesAssertionBlock

val empty = Unit

/**
 * Define a [IsEmpty] assertion block.
 */
infix fun LogRecord.shouldBe(empty: Unit) {
    IsEmpty().check(this)
}

/**
 * Define a [Contains] assertion block.
 */
infix fun LogRecord.shouldContain(block: MessagesAssertionBlock.() -> Unit) {
    Contains().apply(block).check(this)
}

/**
 * Define a [ContainsOnly] assertion block.
 */
infix fun LogRecord.shouldContainOnly(block: MessagesAssertionBlock.() -> Unit) {
    ContainsOnly().apply(block).check(this)
}

/**
 * Define a [ContainsExactly] assertion block.
 */
infix fun LogRecord.shouldContainExactly(block: MessagesAssertionBlock.() -> Unit) {
    ContainsExactly().apply(block).check(this)
}

/**
 * Define a [ContainsInOrder] assertion block.
 */
infix fun LogRecord.shouldContainInOrder(block: MessagesAssertionBlock.() -> Unit) {
    ContainsInOrder().apply(block).check(this)
}
