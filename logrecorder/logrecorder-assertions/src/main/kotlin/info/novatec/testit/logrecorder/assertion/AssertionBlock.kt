package info.novatec.testit.logrecorder.assertion

import info.novatec.testit.logrecorder.api.LogLevel
import info.novatec.testit.logrecorder.api.LogLevel.*
import info.novatec.testit.logrecorder.api.LogRecord

interface AssertionBlock {

    fun trace(message: String) = trace(equalTo(message))
    fun trace(vararg messageMatchers: MessageMatcher) = addExpectation(equalTo(TRACE), messageMatchers.toList())

    fun debug(message: String) = debug(equalTo(message))
    fun debug(vararg messageMatchers: MessageMatcher) = addExpectation(equalTo(DEBUG), messageMatchers.toList())

    fun info(message: String) = info(equalTo(message))
    fun info(vararg messageMatchers: MessageMatcher) = addExpectation(equalTo(INFO), messageMatchers.toList())

    fun warn(message: String) = warn(equalTo(message))
    fun warn(vararg messageMatchers: MessageMatcher) = addExpectation(equalTo(WARN), messageMatchers.toList())

    fun error(message: String) = error(equalTo(message))
    fun error(vararg messageMatchers: MessageMatcher) = addExpectation(equalTo(ERROR), messageMatchers.toList())

    fun any(message: String) = any(equalTo(message))
    fun any(vararg messageMatchers: MessageMatcher) = addExpectation(anyLogLevel(), messageMatchers.toList())

    fun equalTo(message: String) = EqualMessageMatcher(message)
    fun matches(regex: String) = RegexMessageMatcher(regex)
    fun contains(vararg parts: String) = ContainsMessageMatcher(listOf(*parts))
    fun containsInOrder(vararg parts: String) = ContainsInOrderMessageMatcher(listOf(*parts))

    fun startsWith(prefix: String) = StartsWithMessageMatcher(prefix)
    fun endsWith(suffix: String) = EndsWithMessageMatcher(suffix)

    fun equalTo(logLevel: LogLevel) = LogLevelMatcher(logLevel)
    fun anyLogLevel() = LogLevelMatcher(null)

    fun addExpectation(logLevelMatcher: LogLevelMatcher, messageMatchers: List<MessageMatcher>)

    fun check(logRecord: LogRecord)

}
