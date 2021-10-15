package info.novatec.testit.logrecorder.assertion

class ExpectedLogEntry(
    val logLevelMatcher: LogLevelMatcher,
    val messageMatchers: List<MessageMatcher>
)
