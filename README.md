[![License](https://img.shields.io/badge/License-Apache%20License%202.0-brightgreen.svg)](http://www.apache.org/licenses/LICENSE-2.0.txt)
[![Build Status](https://travis-ci.org/nt-ca-aqe/testit-testutils.svg?branch=master)](https://travis-ci.org/nt-ca-aqe/testit-testutils)

# testIT TestUtils

A collection of test-related utility libraries.

## testutils-logrecorder-logback

- JUnit 4 rule for recording Logback loggers
- Junit 5 extension for recording Logback loggers

## testutils-logsuppressor-logback

- JUnit 4 rule for suppressing Logback logging for successful tests
- Junit 5 extension for suppressing Logback logging for successful tests

### Examples

**Class Under Test**
```java
public class SomeService {
        
    private static final Logger log = LoggerFactory.getLogger(SomeService.class);

    void doSomething() {
        log.error("foo");
    }

}
```

**In JUnit 5**
```java
@ExtendWith(LogbackSuppressorExtension.class)
class LogbackSuppressorExtensionDemo {

    SomeService cut = new SomeService();

    @Test
    void thisWillHaveNoLog() {
        cut.doSomething();
    }

    @Test
    void thisWillHaveLogBecauseOfFailure() {
        cut.doSomething();
        Assertions.fail("foo bar");
    }

    @Test
    void thisWillHaveLogBecauseOfException() {
        cut.doSomething();
        throw new IllegalStateException();
    }

}
```

**In JUnit 4**
```java
public class LogbackSuppressorRuleDemo {

    @Rule
    public LogbackSuppressorRule logSuppressor = new LogbackSuppressorRule();

    SomeService cut = new SomeService();

    @Test
    public void thisWillHaveNoLog() {
        cut.doSomething();
    }

    @Test
    public void thisWillHaveLogBecauseOfFailure() {
        cut.doSomething();
        Assert.fail("foo bar");
    }

    @Test
    public void thisWillHaveLogBecauseOfException() {
        cut.doSomething();
        throw new IllegalStateException();
    }

}
```


## testutils-mockito

- JUnit 5 extension which provides the same functionality of the Mockito JUnit 4 runner.
- more to come...

### Licensing
TestUtils is licensed under [The Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.txt).

### Sponsoring
TestUtils is mainly developed by [NovaTec Consulting GmbH](http://www.novatec-gmbh.de/),
a German consultancy firm that drives quality in software development projects.
