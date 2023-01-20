[![License](https://img.shields.io/badge/License-Apache%20License%202.0-brightgreen.svg)](http://www.apache.org/licenses/LICENSE-2.0.txt)

# LogRecorder

JUnit5/Kotest extension(s) as well as pure programmatic for recording and asserting logged messages in tests.

Features:

- Junit5 and Kotest extensions as well as pure programmatic usage for recording:
  - [Logback](https://logback.qos.ch) loggers
  - [Log4j 2](https://logging.apache.org/log4j/2.x/index.html) loggers
  - Java Util Logging (JUL) loggers
- Assertion DSL for validating recorded log messages

See [Documentation](documentation) for more details.

## Artifacts

As of `2022-04-01` all artifacts are published with a Group-ID of `io.github.logrecorder` and the major version was
increased to `2.x.x`. Previously we used to publish under our now deprecated umbrella Group-ID of `info.novatec.testit`.

Since version `2.5.0` a BOM is provided for easy artifact version management.

**Gradle**

```Kotlin
// regular way
dependencies {
    // import a BOM
    testImplementation(platform("io.github.logrecorder:logrecorder-bom:${property("logRecorderVersion")}"))

    // define dependencies without versions
    testImplementation("io.github.logrecorder:logrecorder-junit5")
    testImplementation("io.github.logrecorder:logrecorder-logback")
    testImplementation("io.github.logrecorder:logrecorder-assertions")
}

// Spring Boot dependency management plugin
dependencyManagement {
  imports {
    mavenBom("io.github.logrecorder:logrecorder-bom:${property("logRecorderVersion")}")
  }
}
```

**Maven**

```XML
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>io.github.logrecorder</groupId>
            <artifactId>logrecorder-bom</artifactId>
            <version>${logrecorder.version}</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```

## Licensing

LogRecorder is licensed under [The Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.txt).

## Sponsoring

LogRecorder is mainly developed by [Novatec Consulting GmbH](http://www.novatec-gmbh.de/), a German consultancy firm
that drives quality in software development projects.
