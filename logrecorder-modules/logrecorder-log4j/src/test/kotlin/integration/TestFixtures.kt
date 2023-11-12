package integration

import org.apache.logging.log4j.LogManager

class TestServiceA {
    private val log = LogManager.getLogger(TestServiceA::class.java)

    fun logSomething() {
        log.trace("trace message a")
        log.debug("debug message a")
        log.info("info message a")
        log.warn("warn message a")
        log.error("error message a")
    }

    fun logSingleInfo() {
        log.info("info message a")
    }

    fun logError(exception: Throwable) {
        log.error("error message a", exception)
    }
}

class TestServiceB {
    private val log = LogManager.getLogger(TestServiceB::class.java)
    fun logSomething() {
        log.trace("trace message b")
        log.debug("debug message b")
        log.info("info message b")
        log.warn("warn message b")
        log.error("error message b")
    }
}
