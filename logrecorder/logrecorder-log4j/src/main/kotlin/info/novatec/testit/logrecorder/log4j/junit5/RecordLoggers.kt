/*
 * Copyright 2017-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package info.novatec.testit.logrecorder.log4j.junit5

import org.junit.jupiter.api.extension.ExtendWith
import kotlin.reflect.KClass

/**
 * Annotating any test method with this annotation will start the recording of
 * log messages while the test method is executed.
 *
 * Only those loggers configured with this annotation - either by it's class or
 * name - will be recorded! Multiple loggers can be set by either method and
 * combinations between class and explicit names are possible as well.
 *
 * @see value
 * @see names
 * @since 1.0
 */
@Retention
@Target(AnnotationTarget.FUNCTION)
@ExtendWith(Log4jRecorderExtension::class)
annotation class RecordLoggers(

    /**
     * Classes who's name should be used to identify loggers to record.
     */
    vararg val value: KClass<*> = [],

    /**
     * Names of loggers to record.
     */
    val names: Array<String> = []

)
