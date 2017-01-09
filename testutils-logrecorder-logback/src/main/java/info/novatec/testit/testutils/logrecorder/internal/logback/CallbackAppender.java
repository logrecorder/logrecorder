package info.novatec.testit.testutils.logrecorder.internal.logback;

import java.util.List;
import java.util.function.Consumer;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.Context;
import ch.qos.logback.core.LogbackException;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;
import ch.qos.logback.core.status.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class CallbackAppender implements Appender<ILoggingEvent> {

    @NonNull
    private String name;
    @NonNull
    private Consumer<ILoggingEvent> eventConsumer;

    @Override
    public void doAppend(ILoggingEvent event) throws LogbackException {
        eventConsumer.accept(event);
    }

    @Override
    public void setContext(Context context) {

    }

    @Override
    public Context getContext() {
        return null;
    }

    @Override
    public void addStatus(Status status) {

    }

    @Override
    public void addInfo(String msg) {

    }

    @Override
    public void addInfo(String msg, Throwable ex) {

    }

    @Override
    public void addWarn(String msg) {

    }

    @Override
    public void addWarn(String msg, Throwable ex) {

    }

    @Override
    public void addError(String msg) {

    }

    @Override
    public void addError(String msg, Throwable ex) {

    }

    @Override
    public void addFilter(Filter<ILoggingEvent> newFilter) {

    }

    @Override
    public void clearAllFilters() {

    }

    @Override
    public List<Filter<ILoggingEvent>> getCopyOfAttachedFiltersList() {
        return null;
    }

    @Override
    public FilterReply getFilterChainDecision(ILoggingEvent event) {
        return null;
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public boolean isStarted() {
        return false;
    }

}
