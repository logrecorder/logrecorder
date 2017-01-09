package info.novatec.testit.testutils.logrecorder.junit5;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestExtensionContext;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;

import info.novatec.testit.testutils.logrecorder.api.LogRecord;
import info.novatec.testit.testutils.logrecorder.internal.AbstractLogRecorderExtension;
import info.novatec.testit.testutils.logrecorder.internal.logback.LogbackLog;
import info.novatec.testit.testutils.logrecorder.internal.logback.LogbackLogRecorder;


/**
 * This extension will record the loggers specified by a {@link RecordLoggers} annotation and inject the {@link LogRecord}
 * as
 * a parameter into the test method.
 * <p>
 * Recording a logger will set its log level to {@link Level} to {@link Level#ALL} for the duration of the test. After the
 * test was executed, the log level will be restored to whatever it was before.
 *
 * @see RecordLoggers
 * @see LogRecord
 */
public class LogbackRecorderExtension extends AbstractLogRecorderExtension {

    private static final String RECORDERS = "recorders";
    private static final String ANNOTATION_NOT_FOUND_MSG =
        "no @" + RecordLoggers.class.getSimpleName() + " annotation found on test method!";

    @Override
    public void beforeTestExecution(TestExtensionContext context) {
        initExtensionContext(context);
        getRecorders(context).forEach(LogbackLogRecorder::start);
    }

    @Override
    public void afterTestExecution(TestExtensionContext context) {
        getRecorders(context).forEach(LogbackLogRecorder::stop);
    }

    private void initExtensionContext(TestExtensionContext context) {
        LogbackLog log = new LogbackLog();
        storeLog(context, log);
        List<LogbackLogRecorder> recorders = getLoggers(context)//
            .map(logger -> new LogbackLogRecorder(logger, log))//
            .collect(toList());
        storeRecorders(context, recorders);
    }

    private Stream<Logger> getLoggers(ExtensionContext context) {
        RecordLoggers annotation = context.getTestMethod()
            .map(method -> method.getAnnotation(RecordLoggers.class))
            .orElseThrow(() -> new IllegalStateException(ANNOTATION_NOT_FOUND_MSG));
        List<org.slf4j.Logger> combinedLoggers = new ArrayList<>();
        stream(annotation.value()).map(LoggerFactory::getLogger).forEach(combinedLoggers::add);
        stream(annotation.names()).map(LoggerFactory::getLogger).forEach(combinedLoggers::add);
        return combinedLoggers.stream().map(logger -> ( Logger ) logger);
    }

    private void storeRecorders(ExtensionContext context, List<LogbackLogRecorder> loggerData) {
        getStore(context).put(RECORDERS, loggerData);
    }

    @SuppressWarnings("unchecked")
    private List<LogbackLogRecorder> getRecorders(ExtensionContext context) {
        return ( List<LogbackLogRecorder> ) getStore(context).get(RECORDERS);
    }

}
