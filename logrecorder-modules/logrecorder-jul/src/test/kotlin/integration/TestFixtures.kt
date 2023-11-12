package integration

import java.util.logging.Level
import java.util.logging.Logger

class TestServiceA {
    private val log = Logger.getLogger(TestServiceA::class.java.name)
    fun logSomething() {
        log.finer("trace message a")
        log.fine("debug message a")
        log.info("info message a")
        log.warning("warn message a")
        log.severe("error message a")
    }

    fun logError(exception: Throwable) {
        log.log(Level.SEVERE, "error message a", exception)
    }
}

class TestServiceB {
    private val log = Logger.getLogger(TestServiceB::class.java.name)
    fun logSomething() {
        log.finer("trace message b")
        log.fine("debug message b")
        log.info("info message b")
        log.warning("warn message b")
        log.severe("error message b")
    }
}
