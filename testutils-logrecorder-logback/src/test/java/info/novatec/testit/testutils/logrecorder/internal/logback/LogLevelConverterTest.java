package info.novatec.testit.testutils.logrecorder.internal.logback;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import ch.qos.logback.classic.Level;

import info.novatec.testit.testutils.logrecorder.api.LogLevel;


public class LogLevelConverterTest {

    @TestFactory
    @DisplayName("all logback levels can be converted")
    Stream<DynamicTest> allLevelsCanBeConverted() {
        return new MapBuilder<Level, LogLevel>()//
            .put(Level.TRACE, LogLevel.TRACE)
            .put(Level.DEBUG, LogLevel.DEBUG)
            .put(Level.INFO, LogLevel.INFO)
            .put(Level.WARN, LogLevel.WARN)
            .put(Level.ERROR, LogLevel.ERROR)
            .put(Level.ALL, LogLevel.UNKNOWN)
            .put(Level.OFF, LogLevel.UNKNOWN)
            .stream()
            .map(entry -> dynamicTest(entry.getKey() + " is converted to " + entry.getValue(), () -> {
                assertThat(LogLevelConverter.convert(entry.getKey())).isEqualTo(entry.getValue());
            }));
    }

    public static class MapBuilder<K, V> {

        private final Map<K, V> map = new HashMap<K, V>();

        public MapBuilder<K, V> put(K key, V value) {
            map.put(key, value);
            return this;
        }

        public HashMap<K, V> toMap() {
            return new HashMap<>(map);
        }

        public Set<Entry<K, V>> toEntrySet() {
            return toMap().entrySet();
        }

        public Stream<Entry<K, V>> stream() {
            return toEntrySet().stream();
        }

    }

}
