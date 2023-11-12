package io.github.logrecorder.jul

import io.github.logrecorder.common.LogRecorderContext
import io.github.logrecorder.common.LogRecorderContextFactory
import java.util.logging.LogManager
import java.util.logging.Logger
import kotlin.reflect.KClass

internal class JulLogRecorderContextFactory : LogRecorderContextFactory {

    override fun create(byClasses: Set<KClass<*>>, byNames: Set<String>): LogRecorderContext {
        val fromClasses = byClasses.map(::loggerFromKClass)
        val fromNames = byNames.map(::loggerFromName)

        val loggers = (fromClasses + fromNames).toSet()

        return JulLogRecorderContext(loggers)
    }

    private fun loggerFromKClass(kClass: KClass<*>): Logger = LogManager.getLogManager().getLogger(kClass.java.name)
    private fun loggerFromName(name: String): Logger = LogManager.getLogManager().getLogger(name)
}
