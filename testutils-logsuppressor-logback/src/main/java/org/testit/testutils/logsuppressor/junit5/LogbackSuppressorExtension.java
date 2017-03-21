package org.testit.testutils.logsuppressor.junit5;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.Extension;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Store;
import org.junit.jupiter.api.extension.TestExtensionContext;
import org.slf4j.LoggerFactory;
import org.testit.testutils.logsuppressor.internal.logback.RecordingAppender;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.spi.LifeCycle;


/**
 * This {@link Extension} will suppress all Logback log output by temporarily removing all {@link Appender} from any
 * registered {@link Logger} before executing a test. Instead there will be a {@link RecordingAppender} registered at all
 * loggers which will store any log output until after the a test was executed.
 * <p>
 * In case the test failed (either because of an assertion or an exception) the recorded log events will be provided to all
 * original {@link Appender} in order to to what they would have done under normal circumstances.
 * <p>
 * Note: The original configuration (all {@link Appender} for all {@link Logger}) will be restored after the test.
 */
public class LogbackSuppressorExtension implements BeforeTestExecutionCallback, AfterTestExecutionCallback {

    private static final String ORIGINAL_STATE_MAP = "originalStateMap";
    private static final String REMEMBERING_APPENDER_MAP = "rememberingAppenderMap";

    @Override
    public void beforeTestExecution(TestExtensionContext context) {
        analyzeAndStoreLoggerConfiguration(context);
        replaceAllAppenders(context);
    }

    private void analyzeAndStoreLoggerConfiguration(TestExtensionContext context) {
        Map<Logger, List<Appender<ILoggingEvent>>> originalStateMap = new HashMap<>();
        Map<Logger, RecordingAppender> rememberingAppenderMap = new HashMap<>();
        LoggerContext loggerContext = getLoggerContext();
        loggerContext.getLoggerList().forEach(logger -> {
            List<Appender<ILoggingEvent>> appenders = new ArrayList<>();
            logger.iteratorForAppenders().forEachRemaining(appenders::add);
            if (!appenders.isEmpty()) {
                originalStateMap.put(logger, appenders);
                rememberingAppenderMap.put(logger, new RecordingAppender());
            }
        });
        storeOriginalStateMap(context, originalStateMap);
        storeRememberingAppenderMap(context, rememberingAppenderMap);
    }

    private LoggerContext getLoggerContext() {
        return ( LoggerContext ) LoggerFactory.getILoggerFactory();
    }

    private void replaceAllAppenders(TestExtensionContext context) {
        Map<Logger, RecordingAppender> rememberingAppenderMap = getRememberingAppenderMap(context);
        getOriginalStateMap(context).forEach((logger, appenders) -> {
            logger.detachAndStopAllAppenders();
            logger.addAppender(rememberingAppenderMap.get(logger));
        });
    }

    @Override
    public void afterTestExecution(TestExtensionContext context) {
        restoreOriginalAppenders(context);
        if (theTestFailed(context)) {
            logAllRecordedEvents(context);
        }
    }

    private void restoreOriginalAppenders(TestExtensionContext context) {
        Map<Logger, List<Appender<ILoggingEvent>>> originalStateMap = getOriginalStateMap(context);
        Map<Logger, RecordingAppender> rememberingAppenderMap = getRememberingAppenderMap(context);
        originalStateMap.forEach((logger, originalAppenders) -> {
            logger.detachAndStopAllAppenders();
            originalAppenders.stream()//
                .peek(LifeCycle::start)//
                .forEach(logger::addAppender);
        });
    }

    private boolean theTestFailed(TestExtensionContext context) {
        return context.getTestException().isPresent();
    }

    private void logAllRecordedEvents(TestExtensionContext context) {
        Map<Logger, List<Appender<ILoggingEvent>>> originalStateMap = getOriginalStateMap(context);
        Map<Logger, RecordingAppender> rememberingAppenderMap = getRememberingAppenderMap(context);
        originalStateMap.forEach((logger, originalAppenders) -> {
            rememberingAppenderMap.get(logger).getEvents().forEach(logger::callAppenders);
        });
    }

    private void storeOriginalStateMap(TestExtensionContext context,
        Map<Logger, List<Appender<ILoggingEvent>>> originalStateMap) {
        getStore(context).put(ORIGINAL_STATE_MAP, originalStateMap);
    }

    private void storeRememberingAppenderMap(TestExtensionContext context,
        Map<Logger, RecordingAppender> rememberingAppenderMap) {
        getStore(context).put(REMEMBERING_APPENDER_MAP, rememberingAppenderMap);
    }

    @SuppressWarnings("unchecked")
    private Map<Logger, List<Appender<ILoggingEvent>>> getOriginalStateMap(TestExtensionContext context) {
        return ( Map<Logger, List<Appender<ILoggingEvent>>> ) getStore(context).get(ORIGINAL_STATE_MAP);
    }

    @SuppressWarnings("unchecked")
    private Map<Logger, RecordingAppender> getRememberingAppenderMap(TestExtensionContext context) {
        return ( Map<Logger, RecordingAppender> ) getStore(context).get(REMEMBERING_APPENDER_MAP);
    }

    private Store getStore(TestExtensionContext context) {
        return context.getStore(ExtensionContext.Namespace.create(LogbackSuppressorExtension.class));
    }

}
