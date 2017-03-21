package org.testit.testutils.logsuppressor.junit5;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.inOrder;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.ExtensionContext.Store;
import org.junit.jupiter.api.extension.TestExtensionContext;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.stubbing.Answer;
import org.testit.testutils.logsuppressor.internal.logback.RecordingAppender;
import org.testit.testutils.mockito.junit5.EnableMocking;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@EnableMocking
class LogbackSuppressorExtensionTest {

    @InjectMocks
    LogbackSuppressorExtension cut;

    @Mock
    TestExtensionContext context;
    @Mock
    Store store;

    @Mock
    RecordingAppender appender;

    @BeforeEach
    void mockTestExecutionContextStore() {
        doReturn(store).when(context).getStore(any(Namespace.class));
        Map<?, ?> map = new HashMap<>();
        Answer putAnswer = invocation -> {
            map.put(invocation.getArgument(0), invocation.getArgument(1));
            return null;
        };
        doAnswer(putAnswer).when(store).put(any(), any());
        Answer getAnswer = invocation -> map.get(invocation.getArgument(0));
        doAnswer(getAnswer).when(store).get(any());
    }

    @BeforeEach
    void addMockAppender() {
        (( Logger ) log).addAppender(appender);
    }

    @AfterEach
    void removeMockAppender() {
        (( Logger ) log).detachAppender(appender);
    }

    @Test
    void ifTestIsSuccessfulThereIsNoLog() {
        doReturn(Optional.empty()).when(context).getTestException();
        InOrder inOrder = inOrder(appender);

        cut.beforeTestExecution(context);
        inOrder.verify(appender).stop();
        inOrder.verifyNoMoreInteractions();

        log.info("hello world");
        inOrder.verifyNoMoreInteractions();

        cut.afterTestExecution(context);
        inOrder.verify(appender).start();
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void ifTestFailsThereIsALog() {
        doReturn(Optional.of(new AssertionError())).when(context).getTestException();
        InOrder inOrder = inOrder(appender);

        cut.beforeTestExecution(context);
        inOrder.verify(appender).stop();
        inOrder.verifyNoMoreInteractions();

        log.info("hello world");
        inOrder.verifyNoMoreInteractions();

        cut.afterTestExecution(context);
        inOrder.verify(appender).start();
        inOrder.verify(appender).doAppend(any(ILoggingEvent.class));
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void ifTestHasExceptionThereIsALog() {
        doReturn(Optional.of(new IllegalStateException())).when(context).getTestException();
        InOrder inOrder = inOrder(appender);

        cut.beforeTestExecution(context);
        inOrder.verify(appender).stop();
        inOrder.verifyNoMoreInteractions();

        log.info("hello world");
        inOrder.verifyNoMoreInteractions();

        cut.afterTestExecution(context);
        inOrder.verify(appender).start();
        inOrder.verify(appender).doAppend(any(ILoggingEvent.class));
        inOrder.verifyNoMoreInteractions();
    }

}
