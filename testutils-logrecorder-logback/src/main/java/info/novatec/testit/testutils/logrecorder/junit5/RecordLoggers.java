package info.novatec.testit.testutils.logrecorder.junit5;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.junit.jupiter.api.extension.ExtendWith;


/**
 * Annotating any test method with this annotation will register the {@link LogbackRecorderExtension} with JUnit 5.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith(LogbackRecorderExtension.class)
public @interface RecordLoggers {

    /**
     * @return classes who's name should be used to identify the loggers to record.
     */
    Class<?>[] value() default {};

    /**
     * @return names of the loggers to record.
     */
    String[] names() default {};

}
