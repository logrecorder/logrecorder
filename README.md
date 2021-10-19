[![License](https://img.shields.io/badge/License-Apache%20License%202.0-brightgreen.svg)](http://www.apache.org/licenses/LICENSE-2.0.txt)
[![CircleCI](https://circleci.com/gh/nt-ca-aqe/testit-testutils/tree/master.svg?style=svg)](https://circleci.com/gh/nt-ca-aqe/testit-testutils/tree/master)

# testIT TestUtils

A collection of test-related utility libraries.

## logrecorder-assertions

- Assertion DSL for validating recorded log messages

## logrecorder-logback

- Junit 5 extension for recording Logback loggers
- this dependency includes the logrecorder-assertions

<details>
<summary>Click to show Maven configuration</summary>
```xml
<dependency>
  <groupId>info.novatec.testit</groupId>
  <artifactId>logrecorder-logback</artifactId>
  <version>1.2.0</version>
  <scope>test</scope>
</dependency>
```
</details>

<details>
<summary>Click to show gradle groovy configuration</summary>
```groovy
testImplementation 'info.novatec.testit:logrecorder-logback:1.2.0'
```
</details>

<details>
<summary>Click to show gradle kts configuration</summary>
```groovy
testImplementation("info.novatec.testit:logrecorder-logback:1.2.0")
```
</details>

## logrecorder-log4j

- Junit 5 extension for recording [Log4j 2](http://logging.apache.org/log4j/2.x/index.html) loggers
- this dependency includes the logrecorder-assertions

<details>
<summary>Click to show Maven configuration</summary>
```xml
<dependency>
  <groupId>info.novatec.testit</groupId>
  <artifactId>logrecorder-log4j</artifactId>
  <version>1.2.0</version>
  <scope>test</scope>
</dependency>
```
</details>

<details>
<summary>Click to show gradle groovy configuration</summary>
```groovy
testImplementation 'info.novatec.testit:logrecorder-log4j:1.2.0'
```
</details>

<details>
<summary>Click to show gradle kts configuration</summary>
```groovy
testImplementation("info.novatec.testit:logrecorder-log4j:1.2.0")
```
</details>

## Usage


```kotlin
package info.novatec.testit.logrecorder.log4j.junit5

import info.novatec.testit.logrecorder.api.LogEntry
import info.novatec.testit.logrecorder.api.LogLevel
import info.novatec.testit.logrecorder.api.LogRecord
import info.novatec.testit.logrecorder.log4j.junit5.RecordLoggers
import org.apache.logging.log4j.LogManager
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class Log4JRecorderExtensionTest {

    private val testServiceA = TestServiceA()

    @BeforeEach
    fun logSomethingBeforeTest() {
        testServiceA.logSomething()
    }

    @RecordLoggers(TestServiceA::class) // define from which class you want to test log messages
    @Test
    fun `extension is registered and log messages are recorded`(
        log: LogRecord
    ) {
        assertThat(log.entries).isEmpty()

        testServiceA.logSomething()

        // check only messages
        assertThat(log.messages).containsExactly(
            "trace message a",
            "debug message a",
            "info message a",
            "warn message a",
            "error message a"
        )

        // check message and loglevel
        assertThat(log.entries).containsExactly(
            LogEntry(LogRecord.logger(TestServiceA::class), LogLevel.TRACE, "trace message a"),
            LogEntry(LogRecord.logger(TestServiceA::class), LogLevel.DEBUG, "debug message a"),
            LogEntry(LogRecord.logger(TestServiceA::class), LogLevel.INFO, "info message a"),
            LogEntry(LogRecord.logger(TestServiceA::class), LogLevel.WARN, "warn message a"),
            LogEntry(LogRecord.logger(TestServiceA::class), LogLevel.ERROR, "error message a")
        )
    }
}
```

```java
import static org.assertj.core.api.Assertions.assertThat;

import info.novatec.testit.logrecorder.api.LogEntry;
import info.novatec.testit.logrecorder.api.LogLevel;
import info.novatec.testit.logrecorder.api.LogRecord;
import info.novatec.testit.logrecorder.log4j.junit5.RecordLoggers;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JavaTest {

  private Logger customLogger = LogManager.getLogger("custom-logger");

  private TestServiceA testServiceA = new TestServiceA();
  private TestServiceB testServiceB = new TestServiceB();

  @BeforeEach
  public void logSomethingBeforeTest() {
    testServiceA.logSomething();
    testServiceB.logSomething();
  }

  @RecordLoggers(value = {TestServiceA.class, TestServiceB.class}, names = {"custom-logger"})
  @Test
  public void extensionIsRegisteredAndLogMessagesAreRecorded(LogRecord log) {
    assertThat(log.getEntries()).isEmpty();

    testServiceA.logSomething();

    // check only messages
    assertThat(log.getMessages()).containsExactly(
        "trace message a",
        "debug message a",
        "info message a",
        "warn message a",
        "error message a"
    );

    // check message and loglevel
    assertThat(log.getEntries()).containsExactly(
        new LogEntry(LogRecord.logger(TestServiceA.class), LogLevel.TRACE, "trace message a"),
        new LogEntry(LogRecord.logger(TestServiceA.class), LogLevel.DEBUG, "debug message a"),
        new LogEntry(LogRecord.logger(TestServiceA.class), LogLevel.INFO, "info message a"),
        new LogEntry(LogRecord.logger(TestServiceA.class), LogLevel.WARN, "warn message a"),
        new LogEntry(LogRecord.logger(TestServiceA.class), LogLevel.ERROR, "error message a")
    );

    testServiceB.logSomething();
    assertThat(log.getEntries()).containsExactly(
        new LogEntry(LogRecord.logger(TestServiceA.class), LogLevel.TRACE, "trace message a"),
        new LogEntry(LogRecord.logger(TestServiceA.class), LogLevel.DEBUG, "debug message a"),
        new LogEntry(LogRecord.logger(TestServiceA.class), LogLevel.INFO, "info message a"),
        new LogEntry(LogRecord.logger(TestServiceA.class), LogLevel.WARN, "warn message a"),
        new LogEntry(LogRecord.logger(TestServiceA.class), LogLevel.ERROR, "error message a"),

        new LogEntry(LogRecord.logger(TestServiceB.class), LogLevel.TRACE, "trace message b"),
        new LogEntry(LogRecord.logger(TestServiceB.class), LogLevel.DEBUG, "debug message b"),
        new LogEntry(LogRecord.logger(TestServiceB.class), LogLevel.INFO, "info message b"),
        new LogEntry(LogRecord.logger(TestServiceB.class), LogLevel.WARN, "warn message b"),
        new LogEntry(LogRecord.logger(TestServiceB.class), LogLevel.ERROR, "error message b")
    );

    customLogger.trace("trace message c");
    customLogger.debug("debug message c");
    customLogger.info("info message c");
    customLogger.warn("warn message c");
    customLogger.error("error message c");

    assertThat(log.getEntries()).containsExactly(
        new LogEntry(LogRecord.logger(TestServiceA.class), LogLevel.TRACE, "trace message a"),
        new LogEntry(LogRecord.logger(TestServiceA.class), LogLevel.DEBUG, "debug message a"),
        new LogEntry(LogRecord.logger(TestServiceA.class), LogLevel.INFO, "info message a"),
        new LogEntry(LogRecord.logger(TestServiceA.class), LogLevel.WARN, "warn message a"),
        new LogEntry(LogRecord.logger(TestServiceA.class), LogLevel.ERROR, "error message a"),

        new LogEntry(LogRecord.logger(TestServiceB.class), LogLevel.TRACE, "trace message b"),
        new LogEntry(LogRecord.logger(TestServiceB.class), LogLevel.DEBUG, "debug message b"),
        new LogEntry(LogRecord.logger(TestServiceB.class), LogLevel.INFO, "info message b"),
        new LogEntry(LogRecord.logger(TestServiceB.class), LogLevel.WARN, "warn message b"),
        new LogEntry(LogRecord.logger(TestServiceB.class), LogLevel.ERROR, "error message b"),

        new LogEntry("custom-logger", LogLevel.TRACE, "trace message c"),
        new LogEntry("custom-logger", LogLevel.DEBUG, "debug message c"),
        new LogEntry("custom-logger", LogLevel.INFO, "info message c"),
        new LogEntry("custom-logger", LogLevel.WARN, "warn message c"),
        new LogEntry("custom-logger", LogLevel.ERROR, "error message c")
    );
  }
}
```

### with logrecoder assertions

```kotlin
package info.novatec.testit.logrecorder.assertion

import info.novatec.testit.logrecorder.api.LogEntry
import info.novatec.testit.logrecorder.api.LogLevel
import info.novatec.testit.logrecorder.api.LogLevel.*
import info.novatec.testit.logrecorder.api.LogRecord
import info.novatec.testit.logrecorder.log4j.junit5.RecordLoggers
import info.novatec.testit.logrecorder.assertion.LogRecordAssertion.Companion.assertThat
import org.junit.jupiter.api.*
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource

internal class Log4JRecorderExtensionTest {

    private val testServiceA = TestServiceA()

    @BeforeEach
    fun logSomethingBeforeTest() {
        testServiceA.logSomething()
    }

    @RecordLoggers(TestServiceA::class) // define from which class you want to test log messages
    @Test
    fun `extension is registered and log messages are recorded`(
        log: LogRecord
    ) {
        testServiceA.logSomething()

        assertThat(log) {
            containsExactly {
                trace("trace message")
                debug("debug message")
                info("info message")
                warn("warn message")
                error("error message")
            }
        }
    }
}
```

### Licensing
TestUtils is licensed under [The Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.txt).

### Sponsoring
TestUtils is mainly developed by [Novatec Consulting GmbH](http://www.novatec-gmbh.de/),
a German consultancy firm that drives quality in software development projects.
