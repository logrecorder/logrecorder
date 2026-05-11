# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Test Commands

Always pass `-q -ntp` to Maven to suppress verbose output (`-q` shows errors only, `-ntp` suppresses download/upload progress).

```bash
# Build and verify (includes license header check)
./mvnw -q -ntp clean verify

# Run all tests
./mvnw -q -ntp test

# Run a single test class
./mvnw -q -ntp test -Dtest=ClassName

# Run a single test method
./mvnw -q -ntp test -Dtest=ClassName#methodName

# Run tests in a specific module
./mvnw -q -ntp test -pl logrecorder-modules/logrecorder-logback
```

## Architecture

Multi-module Maven project. Kotlin 1.8 / Java 11 target.

**Module dependency order** (leaf → root):

```
logrecorder-api          ← public interfaces (LogRecord, LogEntry, LogLevel)
logrecorder-common       ← internal SPI (LogRecorderContext, LogRecorderContextFactory)
logrecorder-assertions   ← AssertJ-based DSL for validating recorded entries
logrecorder-junit5       ← JUnit 5 extension (@RecordLoggers, parameter injection)
logrecorder-kotest       ← Kotest extension (coroutine-aware)
logrecorder-logback      ← Logback implementation (attaches custom Appender)
logrecorder-log4j        ← Log4j 2 implementation (attaches custom Appender)
logrecorder-jul          ← java.util.logging implementation (attaches custom Handler)
logrecorder-bom          ← BOM for consumers; no source
```

**Runtime wiring**: Framework implementations register `LogRecorderContextFactory` via `java.util.ServiceLoader` (META-INF/services). `logrecorder-common` discovers them at runtime — no compile-time coupling between test-framework modules and logging-framework modules.

**Recording lifecycle**: JUnit5/Kotest extensions call `LogRecorderContext.start()` before the test and `stop()` after. Each logging-framework module attaches an appender/handler to the target logger during `start()`, collects `LogEntry` objects, then detaches on `stop()`. The collected entries are exposed via the `LogRecord` API.

**Key types**:
- `LogEntry` — immutable data: logger name, `LogLevel`, message, marker, `Map<String, Any>` properties, optional throwable
- `LogRecord` — query interface over collected entries (filter by level, logger, message)
- `LogRecorderContext` — internal: holds a `LogRecord`, controls start/stop
- `LogRecorderContextFactory` — SPI implemented once per logging framework

## Conventions

- All production and test code is Kotlin.
- License headers are enforced by `license-maven-plugin`; new `.kt` files need the Apache 2.0 header (see any existing source file for the template).
- Published to Maven Central under group `io.github.logrecorder`. Version is managed in the root `pom.xml`.
