package info.novatec.testit.testutils.logrecorder.junit4;

import static info.novatec.testit.testutils.logrecorder.assertj.LogRecorderAssertions.assertThat;
import static org.assertj.core.api.Assertions.atIndex;

import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class LogbackRecorderRuleIntTest {

    LoggingClass loggingClass1 = new LoggingClass();
    AnotherLoggingClass loggingClass2 = new AnotherLoggingClass();

    @Rule
    public LogbackRecorderRule recorderForClasses = //
        new LogbackRecorderRule(LoggingClass.class, AnotherLoggingClass.class);
    @Rule
    public LogbackRecorderRule recorderForStrings =
        new LogbackRecorderRule(LoggingClass.class.getName(), AnotherLoggingClass.class.getName());

    @Test
    public void recordedLoggersCanBeDeclaredByClassReference() {
        loggingClass1.doSomeThing();
        assertThat(recorderForClasses.getLog()).messages()
            .contains("trace log", atIndex(0))
            .contains("debug log", atIndex(1))
            .contains("info log", atIndex(2))
            .contains("warning log", atIndex(3))
            .contains("error log", atIndex(4));
        loggingClass2.doSomeThing();
        assertThat(recorderForClasses.getLog()).messages()//
            .contains("another info log", atIndex(5));
    }

    @Test
    public void recordedLoggersCanBeDeclaredByName() {
        loggingClass1.doSomeThing();
        assertThat(recorderForStrings.getLog()).messages()
            .contains("trace log", atIndex(0))
            .contains("debug log", atIndex(1))
            .contains("info log", atIndex(2))
            .contains("warning log", atIndex(3))
            .contains("error log", atIndex(4));
        loggingClass2.doSomeThing();
        assertThat(recorderForStrings.getLog()).messages()//
            .contains("another info log", atIndex(5));
    }

    static class LoggingClass {
        Logger logger = LoggerFactory.getLogger(getClass());

        void doSomeThing() {
            logger.trace("trace log");
            logger.debug("debug log");
            logger.info("info log");
            logger.warn("warning log");
            logger.error("error log");
        }
    }

    static class AnotherLoggingClass {
        Logger logger = LoggerFactory.getLogger(getClass());

        void doSomeThing() {
            logger.info("another info log");
        }
    }

}
