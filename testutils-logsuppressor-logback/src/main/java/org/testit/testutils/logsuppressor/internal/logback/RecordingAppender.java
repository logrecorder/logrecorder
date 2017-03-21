package org.testit.testutils.logsuppressor.internal.logback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.Context;
import ch.qos.logback.core.LogbackException;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;
import ch.qos.logback.core.status.Status;
import lombok.Getter;
import lombok.Setter;


/**
 * This {@link Appender} records all {@link ILoggingEvent} instances passed to it.
 * These events can be retrieved later in order to process them further.
 */
public class RecordingAppender implements Appender<ILoggingEvent> {

    @Getter
    @Setter
    private String name = UUID.randomUUID().toString();
    private List<ILoggingEvent> events = new ArrayList<>();

    public List<ILoggingEvent> getEvents() {
        return Collections.unmodifiableList(events);
    }

    @Override
    public void doAppend(ILoggingEvent event) throws LogbackException {
        events.add(event);
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
