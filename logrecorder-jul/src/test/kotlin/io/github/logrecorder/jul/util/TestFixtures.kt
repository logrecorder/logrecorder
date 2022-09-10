package io.github.logrecorder.jul.util

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
