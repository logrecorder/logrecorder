package io.github.logrecorder.logback

import ch.qos.logback.classic.Logger
import io.github.logrecorder.common.LogRecorderContext
import io.github.logrecorder.common.LogRecorderContextFactory
import org.slf4j.LoggerFactory
import kotlin.reflect.KClass

internal class LogbackLogRecorderContextFactory : LogRecorderContextFactory {

    override fun create(byClasses: Set<KClass<*>>, byNames: Set<String>): LogRecorderContext {
        val fromClasses = byClasses.map(::loggerFromKClass)
        val fromNames = byNames.map(::loggerFromName)

        val loggers = (fromClasses + fromNames).toSet()

        return LogbackLogRecorderContext(loggers)
    }

    private fun loggerFromKClass(kClass: KClass<*>): Logger = LoggerFactory.getLogger(kClass.java) as Logger
    private fun loggerFromName(name: String): Logger = LoggerFactory.getLogger(name) as Logger
}
