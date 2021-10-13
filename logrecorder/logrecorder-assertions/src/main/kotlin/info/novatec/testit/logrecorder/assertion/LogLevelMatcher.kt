package info.novatec.testit.logrecorder.assertion

import info.novatec.testit.logrecorder.api.LogLevel

internal class LogLevelMatcher(
    val level: LogLevel?
) {
    fun matches(actual: LogLevel): Boolean {
        if (level == null) return true
        return actual == level
    }

    override fun toString(): String = level?.name ?: "ANY"
}
