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
package info.novatec.testit.logrecorder.api

import info.novatec.testit.logrecorder.api.LogLevel.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class LogRecordTest {

    val entries1 = LogEntry(logger = "logger-a", level = INFO, message = "Message #1", marker = "Marker #1")
    val entries2 = LogEntry(logger = "logger-a", level = INFO, message = "Message #2", marker = "Marker #2")
    val entries3 = LogEntry(logger = "logger-a", level = DEBUG, message = "Message #3", marker = "Marker #3")
    val entries4 = LogEntry(logger = "logger-b", level = DEBUG, message = "Message #4", marker = "Marker #4")
    val entries5 = LogEntry(logger = "logger-b", level = INFO, message = "Message #5", marker = "Marker #5")

    @DisplayName("all recorded messages can be got")
    @Nested inner class Messages {

        @Test fun `they will be in same order as the entries`() {
            val cut = logRecord(entries1, entries2, entries3, entries4, entries5)
            assertThat(cut.messages)
                .containsExactly("Message #1", "Message #2", "Message #3", "Message #4", "Message #5")
        }

        @Test fun `if there are no entries an empty list is returned`() {
            val cut = logRecord()
            assertThat(cut.messages).isEmpty()
        }
    }

    @DisplayName("all recorded markers can be got")
    @Nested inner class Markers {

        @Test fun `they will be in same order as the entries`() {
            val cut = logRecord(entries1, entries2, entries3, entries4, entries5)
            assertThat(cut.markers)
                .containsExactly("Marker #1", "Marker #2", "Marker #3", "Marker #4", "Marker #5")
        }

        @Test fun `if there are no entries an empty list is returned`() {
            val cut = logRecord()
            assertThat(cut.markers).isEmpty()
        }

        @Test
        fun `null markers are returned`() {
            val markerNull = entries1.copy(marker = null)
            val cutWithNull = logRecord(markerNull)

            assertThat(cutWithNull.markers).containsExactly(null)
        }
    }

    @DisplayName("recorded messages can be got filtered")
    @Nested inner class FilteredMessages {
        val cut = logRecord(entries1, entries2, entries3, entries4, entries5)

        @Test fun `by logger`() {
            assertThat(cut.messages(logger = "logger-a")).containsExactly("Message #1", "Message #2", "Message #3")
            assertThat(cut.messages(logger = "logger-b")).containsExactly("Message #4", "Message #5")
            assertThat(cut.messages(logger = "logger-c")).isEmpty()
        }

        @Test fun `by level`() {
            assertThat(cut.messages(level = INFO)).containsExactly("Message #1", "Message #2", "Message #5")
            assertThat(cut.messages(level = DEBUG)).containsExactly("Message #3", "Message #4")
            assertThat(cut.messages(level = WARN)).isEmpty()
        }

        @Test fun `by logger and level`() {
            assertThat(cut.messages(logger = "logger-a", level = INFO)).containsExactly("Message #1", "Message #2")
        }

    }


    @DisplayName("recorded markers can be got filtered")
    @Nested inner class FilteredMarkers {

        val nullMarkerEntry = LogEntry(logger = "logger-d", level = INFO, message = "Nullmarker")
        val cut = logRecord(entries1, entries2, entries3, entries4, entries5, nullMarkerEntry)

        @Test fun `filters entries without markers`() {
            assertThat(cut.markers(logger = "logger-d")).isEmpty()
        }

        @Test fun `by logger`() {
            assertThat(cut.markers(logger = "logger-a")).containsExactly("Marker #1", "Marker #2", "Marker #3")
            assertThat(cut.markers(logger = "logger-b")).containsExactly("Marker #4", "Marker #5")
            assertThat(cut.markers(logger = "logger-c")).isEmpty()
        }

        @Test fun `by level`() {
            assertThat(cut.markers(level = INFO)).containsExactly("Marker #1", "Marker #2", "Marker #5")
            assertThat(cut.markers(level = DEBUG)).containsExactly("Marker #3", "Marker #4")
            assertThat(cut.markers(level = WARN)).isEmpty()
        }

        @Test fun `by logger and level`() {
            assertThat(cut.markers(logger = "logger-a", level = INFO)).containsExactly("Marker #1", "Marker #2")
        }
    }

    @DisplayName("recorded entries can be got filtered")
    @Nested inner class FilteredEntries {

        val cut = logRecord(entries1, entries2, entries3, entries4, entries5)

        @Test fun `by logger`() {
            assertThat(cut.entries(logger = "logger-a")).containsExactly(entries1, entries2, entries3)
            assertThat(cut.entries(logger = "logger-b")).containsExactly(entries4, entries5)
            assertThat(cut.entries(logger = "logger-c")).isEmpty()
        }

        @Test fun `by level`() {
            assertThat(cut.entries(level = INFO)).containsExactly(entries1, entries2, entries5)
            assertThat(cut.entries(level = DEBUG)).containsExactly(entries3, entries4)
            assertThat(cut.entries(level = WARN)).isEmpty()
        }

        @Test fun `by logger and level`() {
            assertThat(cut.entries(logger = "logger-a", level = INFO)).containsExactly(entries1, entries2)
        }

    }

    @DisplayName("logger names can be generated from classes")
    @Nested inner class LoggerNames {

        @Test fun `Kotlin Class`() {
            val logger = LogRecord.logger(TestLogRecord::class)
            assertThat(logger).isEqualTo("info.novatec.testit.logrecorder.api.TestLogRecord")
        }

        @Test fun `Java Class`() {
            val logger = LogRecord.logger(TestLogRecord::class.java)
            assertThat(logger).isEqualTo("info.novatec.testit.logrecorder.api.TestLogRecord")
        }

    }

    fun logRecord(vararg entries: LogEntry): LogRecord = TestLogRecord(listOf(*entries))

}
