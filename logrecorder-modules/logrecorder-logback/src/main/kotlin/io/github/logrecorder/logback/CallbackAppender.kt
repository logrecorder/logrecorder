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
package io.github.logrecorder.logback

import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.Appender
import ch.qos.logback.core.Context
import ch.qos.logback.core.filter.Filter
import ch.qos.logback.core.spi.FilterReply
import ch.qos.logback.core.status.Status

internal class CallbackAppender(
    private var name: String,
    private val eventConsumer: (ILoggingEvent) -> Unit
) : Appender<ILoggingEvent> {

    override fun getName(): String {
        return name
    }

    override fun setName(name: String) {
        this.name = name
    }

    override fun doAppend(event: ILoggingEvent) {
        eventConsumer(event)
    }

    override fun setContext(context: Context) = Unit
    override fun getContext(): Context? = null
    override fun addStatus(status: Status) = Unit
    override fun addInfo(msg: String) = Unit
    override fun addInfo(msg: String, ex: Throwable) = Unit
    override fun addWarn(msg: String) = Unit
    override fun addWarn(msg: String, ex: Throwable) = Unit
    override fun addError(msg: String) = Unit
    override fun addError(msg: String, ex: Throwable) = Unit
    override fun addFilter(newFilter: Filter<ILoggingEvent>) = Unit
    override fun clearAllFilters() = Unit
    override fun getCopyOfAttachedFiltersList(): List<Filter<ILoggingEvent>>? = null
    override fun getFilterChainDecision(event: ILoggingEvent): FilterReply? = null
    override fun start() = Unit
    override fun stop() = Unit
    override fun isStarted(): Boolean = false

}
