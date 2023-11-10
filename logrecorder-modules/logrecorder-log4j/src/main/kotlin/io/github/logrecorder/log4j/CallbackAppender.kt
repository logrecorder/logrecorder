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
package io.github.logrecorder.log4j

import org.apache.logging.log4j.core.Appender
import org.apache.logging.log4j.core.Core
import org.apache.logging.log4j.core.Filter
import org.apache.logging.log4j.core.Layout
import org.apache.logging.log4j.core.LogEvent
import org.apache.logging.log4j.core.appender.AbstractAppender
import org.apache.logging.log4j.core.config.plugins.Plugin

@Plugin(
    name = "CallbackAppender",
    category = Core.CATEGORY_NAME,
    elementType = Appender.ELEMENT_TYPE
)
internal class CallbackAppender(
    name: String,
    filter: Filter?,
    layout: Layout<String>?,
    private val eventConsumer: (LogEvent) -> Unit
) : AbstractAppender(name, filter, layout, false, null) {

    override fun append(event: LogEvent) {
        eventConsumer(event)
    }

}
