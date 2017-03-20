package org.testit.testutils.mockito.junit5;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.junit.jupiter.api.extension.ExtendWith;


/**
 * Annotating a JUnit 5 test class with this annotation will trigger the registration of the {@link MockitoExtension}.
 *
 * @see MockitoExtension
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith(MockitoExtension.class)
public @interface EnableMocking {
}
