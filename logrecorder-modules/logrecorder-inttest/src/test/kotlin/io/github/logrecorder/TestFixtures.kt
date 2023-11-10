package io.github.logrecorder

import org.apache.logging.log4j.LogManager
import org.slf4j.LoggerFactory
import java.util.logging.Logger

class TestServiceLog4J {
    private val log = LogManager.getLogger(TestServiceLog4J::class.java)

    fun logSomething() {
        log.trace("trace message a")
        log.debug("debug message a")
        log.info("info message a")
        log.warn("warn message a")
        log.error("error message a")
    }
}

class TestServiceLogback {
    private val log = LoggerFactory.getLogger(javaClass)

    fun logSomething() {
        log.trace("trace message a")
        log.debug("debug message a")
        log.info("info message a")
        log.warn("warn message a")
        log.error("error message a")
    }
}

class TestServiceJul {
    private val log = Logger.getLogger(TestServiceJul::class.java.name)

    fun logSomething() {
        log.finer("trace message a")
        log.fine("debug message a")
        log.info("info message a")
        log.warning("warn message a")
        log.severe("error message a")
    }
}
