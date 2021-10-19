/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package info.novatec.testit.logrecorder.log4j.junit5

import info.novatec.testit.logrecorder.api.LogEntry
import info.novatec.testit.logrecorder.api.LogLevel
import info.novatec.testit.logrecorder.api.LogRecord
import info.novatec.testit.logrecorder.assertion.LogRecordAssertion
import info.novatec.testit.logrecorder.assertion.containsExactly
import org.apache.logging.log4j.LogManager
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class Log4JRecorderExtensionTest {

    private val customLogger = LogManager.getLogger("custom-logger")

    private val testServiceA = TestServiceA()
    private val testServiceB = TestServiceB()

    @BeforeEach
    fun logSomethingBeforeTest() {
        testServiceA.logSomething()
        testServiceB.logSomething()
    }

    @RecordLoggers(TestServiceA::class, TestServiceB::class, names = ["custom-logger"])
    @Test
    fun `extension is registered and log messages are recorded`(log: LogRecord) {
        assertThat(log.entries).isEmpty()

        testServiceA.logSomething()
        assertThat(log.messages).containsExactly(
            "trace message a",
            "debug message a",
            "info message a",
            "warn message a",
            "error message a"
        )

        testServiceB.logSomething()
        LogRecordAssertion.assertThat(log) {
            containsExactly {
                trace("trace message a")
                debug("debug message a")
                info("info message a")
                warn("warn message a")
                error("error message a")

                trace("trace message b")
                debug("debug message b")
                info("info message b")
                warn("warn message b")
                error("error message b")
            }
        }

        customLogger.trace("trace message c")
        customLogger.debug("debug message c")
        customLogger.info("info message c")
        customLogger.warn("warn message c")
        customLogger.error("error message c")

        LogRecordAssertion.assertThat(log) {
            containsExactly {
                trace("trace message a")
                debug("debug message a")
                info("info message a")
                warn("warn message a")
                error("error message a")

                trace("trace message b")
                debug("debug message b")
                info("info message b")
                warn("warn message b")
                error("error message b")

                trace("trace message c")
                debug("debug message c")
                info("info message c")
                warn("warn message c")
                error("error message c")
            }
        }
    }

    @RecordLoggers(TestServiceA::class)
    @Test
    fun `extension is registered and log messages are recorded from ServiceA`(log: LogRecord) {
        assertThat(log.entries).isEmpty()

        testServiceA.logSomething()
        LogRecordAssertion.assertThat(log) {
            containsExactly {
                trace("trace message a")
                debug("debug message a")
                info("info message a")
                warn("warn message a")
                error("error message a")
            }
        }

        testServiceB.logSomething()
        LogRecordAssertion.assertThat(log) {
            containsExactly {
                trace("trace message a")
                debug("debug message a")
                info("info message a")
                warn("warn message a")
                error("error message a")
            }
        }

        customLogger.trace("trace message c")
        customLogger.debug("debug message c")
        customLogger.info("info message c")
        customLogger.warn("warn message c")
        customLogger.error("error message c")

        LogRecordAssertion.assertThat(log) {
            containsExactly {
                trace("trace message a")
                debug("debug message a")
                info("info message a")
                warn("warn message a")
                error("error message a")
            }
        }
    }

}

class TestServiceA {
    private val log = LogManager.getLogger(TestServiceA::class.java)
    fun logSomething() {
        log.trace("trace message a")
        log.debug("debug message a")
        log.info("info message a")
        log.warn("warn message a")
        log.error("error message a")
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
