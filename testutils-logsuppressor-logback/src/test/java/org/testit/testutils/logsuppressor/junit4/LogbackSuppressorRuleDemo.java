package org.testit.testutils.logsuppressor.junit4;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import lombok.extern.slf4j.Slf4j;


public class LogbackSuppressorRuleDemo {

    @Rule
    public LogbackSuppressorRule logSuppressor = new LogbackSuppressorRule();

    SomeService service = new SomeService();

    @Test
    public void thisWillHaveNoLog() {
        service.doSomething();
    }

    @Test
    public void thisWillHaveLogBecauseOfFailure() {
        service.doSomething();
        Assert.fail("foo bar");
    }

    @Test
    public void thisWillHaveLogBecauseOfException() {
        service.doSomething();
        throw new IllegalStateException();
    }

    @Slf4j
    static class SomeService {

        void doSomething() {
            log.error("foo");
        }

    }

}
