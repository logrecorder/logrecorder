package io.github.logrecorder.common

import kotlin.reflect.KClass

interface LogRecorderContextFactory {
    fun create(byClasses: Set<KClass<*>>, byNames: Set<String>): LogRecorderContext
}
