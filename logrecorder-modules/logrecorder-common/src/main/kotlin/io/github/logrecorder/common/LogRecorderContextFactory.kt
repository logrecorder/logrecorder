package io.github.logrecorder.common

import kotlin.reflect.KClass

/**
 * Internal interface implemented by log-framework modules to create a [LogRecorderContext].
 *
 * Not intended to be implemented outside the core modules.
 * We might change this interface at any time.
 */
interface LogRecorderContextFactory {
    fun create(byClasses: Set<KClass<*>>, byNames: Set<String>): LogRecorderContext
}
