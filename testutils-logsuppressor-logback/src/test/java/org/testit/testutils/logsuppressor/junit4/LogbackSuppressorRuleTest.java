package org.testit.testutils.logsuppressor.junit4;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.inOrder;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.model.Statement;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.testit.testutils.logsuppressor.internal.logback.RecordingAppender;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RunWith(MockitoJUnitRunner.class)
public class LogbackSuppressorRuleTest {

    @InjectMocks
    LogbackSuppressorRule cut;

    @Mock
    RecordingAppender appender;

    @Before
    public void addMockAppender() {
        (( Logger ) log).addAppender(appender);
    }

    @After
    public void removeMockAppender() {
        (( Logger ) log).detachAppender(appender);
    }

    @Test
    public void ifTestIsSuccessfulThereIsNoLog() throws Throwable {
        Statement statement = new Statement() {
            @Override
            public void evaluate() throws Throwable {
                log.info("hello world");
            }
        };

        cut.apply(statement, null).evaluate();

        InOrder inOrder = inOrder(appender);
        inOrder.verify(appender).stop();
        inOrder.verify(appender).start();
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void ifTestFailsThereIsALog() throws Throwable {
        Statement statement = new Statement() {
            @Override
            public void evaluate() throws Throwable {
                log.info("hello world");
                throw new AssertionError();
            }
        };

        try {
            cut.apply(statement, null).evaluate();
        } catch (AssertionError e) {
            // ignore
        }

        InOrder inOrder = inOrder(appender);
        inOrder.verify(appender).stop();
        inOrder.verify(appender).start();
        inOrder.verify(appender).doAppend(any(ILoggingEvent.class));
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    public void ifTestHasExceptionThereIsALog() throws Throwable {
        Statement statement = new Statement() {
            @Override
            public void evaluate() throws Throwable {
                log.info("hello world");
                throw new Exception();
            }
        };

        try {
            cut.apply(statement, null).evaluate();
        } catch (Exception e) {
            // ignore
        }

        InOrder inOrder = inOrder(appender);
        inOrder.verify(appender).stop();
        inOrder.verify(appender).start();
        inOrder.verify(appender).doAppend(any(ILoggingEvent.class));
        inOrder.verifyNoMoreInteractions();
    }

}
