package de.novatec.logrecorder.assertion

import de.novatec.logrecorder.api.LogLevel

internal class LogLevelMatcher(
    val level: LogLevel?
) {
    fun matches(actual: LogLevel): Boolean {
        if (level == null) return true
        return actual == level
    }

    override fun toString(): String = level?.name ?: "ANY"
}