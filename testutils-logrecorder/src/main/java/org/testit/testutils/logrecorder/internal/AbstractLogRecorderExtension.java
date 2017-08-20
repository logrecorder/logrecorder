package org.testit.testutils.logrecorder.internal;

import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.ExtensionContext.Store;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.testit.testutils.logrecorder.api.LogRecord;


public abstract class AbstractLogRecorderExtension
    implements BeforeTestExecutionCallback, AfterTestExecutionCallback, ParameterResolver {

    private static final String LOG = "log";

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
        return LogRecord.class.equals(parameterContext.getParameter().getType());
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext context) {
        return getLog(context);
    }

    protected void storeLog(ExtensionContext context, AbstractLog recorder) {
        getStore(context).put(LOG, recorder);
    }

    private AbstractLog getLog(ExtensionContext context) {
        return getStore(context).get(LOG, AbstractLog.class);
    }

    protected Store getStore(ExtensionContext context) {
        return context.getStore(Namespace.create(getClass()));
    }

}
