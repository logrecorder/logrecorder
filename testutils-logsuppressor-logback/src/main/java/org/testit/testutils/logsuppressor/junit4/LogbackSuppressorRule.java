package org.testit.testutils.logsuppressor.junit4;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Rule;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.slf4j.LoggerFactory;
import org.testit.testutils.logsuppressor.internal.logback.RecordingAppender;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.spi.LifeCycle;
import lombok.RequiredArgsConstructor;


/**
 * This {@link Rule} will suppress all Logback log output by temporarily removing all {@link Appender} from any
 * registered {@link Logger} before executing a test. Instead there will be a {@link RecordingAppender} registered at all
 * loggers which will store any log output until after the a test was executed.
 * <p>
 * In case the test failed (either because of an assertion or an exception) the recorded log events will be provided to all
 * original {@link Appender} in order to to what they would have done under normal circumstances.
 * <p>
 * Note: The original configuration (all {@link Appender} for all {@link Logger}) will be restored after the test.
 */
public class LogbackSuppressorRule implements TestRule {

    @Override
    public Statement apply(Statement base, Description description) {
        return new LogbackSuppressorStatement(base);
    }

    @RequiredArgsConstructor
    private static class LogbackSuppressorStatement extends Statement {

        private final Statement next;

        private final Map<Logger, List<Appender<ILoggingEvent>>> originalStateMap = new HashMap<>();
        private final Map<Logger, RecordingAppender> rememberingAppenderMap = new HashMap<>();

        @Override
        public void evaluate() throws Throwable {
            analyzeAndStoreLoggerConfiguration();
            replaceAllAppenders();

            Throwable throwable = null;
            try {
                next.evaluate();
            } catch (AssertionError | Exception e) {
                throwable = e;
            } finally {
                restoreOriginalAppenders();
            }

            if (throwable != null) {
                logAllRecordedEvents();
                throw throwable;
            }
        }

        private void analyzeAndStoreLoggerConfiguration() {
            LoggerContext loggerContext = getLoggerContext();
            loggerContext.getLoggerList().forEach(logger -> {
                List<Appender<ILoggingEvent>> appenders = new ArrayList<>();
                logger.iteratorForAppenders().forEachRemaining(appenders::add);
                if (!appenders.isEmpty()) {
                    originalStateMap.put(logger, appenders);
                    rememberingAppenderMap.put(logger, new RecordingAppender());
                }
            });
        }

        private LoggerContext getLoggerContext() {
            return ( LoggerContext ) LoggerFactory.getILoggerFactory();
        }

        private void replaceAllAppenders() {
            originalStateMap.forEach((logger, appenders) -> {
                logger.detachAndStopAllAppenders();
                logger.addAppender(rememberingAppenderMap.get(logger));
            });
        }

        private void restoreOriginalAppenders() {
            originalStateMap.forEach((logger, originalAppenders) -> {
                logger.detachAndStopAllAppenders();
                originalAppenders.stream()//
                    .peek(LifeCycle::start)//
                    .forEach(logger::addAppender);
            });
        }

        private void logAllRecordedEvents() {
            originalStateMap.forEach((logger, originalAppenders) -> {
                rememberingAppenderMap.get(logger).getEvents().forEach(logger::callAppenders);
            });
        }

    }

}
