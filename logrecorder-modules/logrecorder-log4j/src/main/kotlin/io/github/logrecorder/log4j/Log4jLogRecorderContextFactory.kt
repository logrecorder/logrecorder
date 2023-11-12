package io.github.logrecorder.log4j

import io.github.logrecorder.common.LogRecorderContext
import io.github.logrecorder.common.LogRecorderContextFactory
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import kotlin.reflect.KClass

internal class Log4jLogRecorderContextFactory : LogRecorderContextFactory {

    override fun create(byClasses: Set<KClass<*>>, byNames: Set<String>): LogRecorderContext {
        val fromClasses = byClasses.map(::loggerFromKClass)
        val fromNames = byNames.map(::loggerFromName)

        val loggers = (fromClasses + fromNames).toSet()

        return Log4jLogRecorderContext(loggers)
    }

    private fun loggerFromKClass(kClass: KClass<*>): Logger = LogManager.getLogger(kClass.java)
    private fun loggerFromName(name: String): Logger = LogManager.getLogger(name)
}
