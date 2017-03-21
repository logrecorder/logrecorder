package org.testit.testutils.logsuppressor.junit5;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import lombok.extern.slf4j.Slf4j;


@ExtendWith(LogbackSuppressorExtension.class)
class LogbackSuppressorExtensionDemo {

    SomeService service = new SomeService();

    @Test
    void thisWillHaveNoLog() {
        service.doSomething();
    }

    @Test
    void thisWillHaveLogBecauseOfFailure() {
        service.doSomething();
        Assertions.fail("foo bar");
    }

    @Test
    void thisWillHaveLogBecauseOfException() {
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
