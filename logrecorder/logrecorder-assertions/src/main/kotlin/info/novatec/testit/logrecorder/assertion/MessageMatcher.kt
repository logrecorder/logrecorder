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
package info.novatec.testit.logrecorder.assertion

abstract class MessageMatcher {
    abstract fun matches(actual: String): Boolean
}

class EqualMessageMatcher(
    private val message: String
) : MessageMatcher() {
    override fun matches(actual: String): Boolean = actual == message
    override fun toString(): String = """equalTo ["$message"]"""
}

class RegexMessageMatcher(
    private val regex: String
) : MessageMatcher() {
    override fun matches(actual: String): Boolean = Regex(regex).matches(actual)
    override fun toString(): String = """matches ["$regex"]"""
}

class ContainsMessageMatcher(
    private val parts: List<String>
) : MessageMatcher() {
    override fun matches(actual: String): Boolean = parts.all { actual.contains(it) }
    override fun toString(): String = """contains [${parts.joinToString { "\"$it\"" }}]"""
}

class ContainsInOrderMessageMatcher(
    private val parts: List<String>
) : MessageMatcher() {
    override fun matches(actual: String): Boolean {
        val indices = parts.map { actual.indexOf(it) }
        if (indices.any { it < 0 }) {
            return false
        }
        return indices == indices.sorted()
    }

    override fun toString(): String = """contains in order [${parts.joinToString { "\"$it\"" }}]"""
}

class StartsWithMessageMatcher(
    private val prefix: String
) : MessageMatcher() {
    override fun matches(actual: String): Boolean = actual.startsWith(prefix)
    override fun toString(): String = """starts with ["$prefix"]"""
}

class EndsWithMessageMatcher(
    private val suffix: String
) : MessageMatcher() {
    override fun matches(actual: String): Boolean = actual.endsWith(suffix)
    override fun toString(): String = """ends with ["$suffix"]"""
}
