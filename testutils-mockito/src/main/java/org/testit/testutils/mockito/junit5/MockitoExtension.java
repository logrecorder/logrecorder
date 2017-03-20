package org.testit.testutils.mockito.junit5;

import org.junit.jupiter.api.extension.Extension;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;


/**
 * This JUnit 5 {@link Extension} implements {@link TestInstancePostProcessor} and will execute {@link
 * MockitoAnnotations#initMocks(Object)} for each test instance.
 * <p>
 * This will produce the same behaviour as Mockito's {@link MockitoJUnitRunner} did for JUnit 4.
 *
 * @see TestInstancePostProcessor
 * @see MockitoAnnotations
 */
public class MockitoExtension implements TestInstancePostProcessor {

    @Override
    public void postProcessTestInstance(Object testInstance, ExtensionContext context) throws Exception {
        MockitoAnnotations.initMocks(testInstance);
    }

}
