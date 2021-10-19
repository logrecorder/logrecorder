package info.novatec.testit.logrecorder.assertion

import info.novatec.testit.logrecorder.api.LogRecord

@DslContext
class LogRecordAssertion(
    private val logRecord: LogRecord
) {

    private val blocks = mutableListOf<AssertionBlock>()

    @Deprecated("replaced with extension function `containsExactly`")
    fun containsInOrder(block: ContainsExactly.() -> Unit) =
        addAssertionBlock(ContainsExactly().apply(block))

    fun addAssertionBlock(block: AssertionBlock) {
        blocks.add(block)
    }

    private fun check() {
        blocks.forEach { it.check(logRecord) }
    }

    companion object {
        @JvmStatic
        fun assertThat(logRecord: LogRecord, block: LogRecordAssertion.() -> Unit) {
            LogRecordAssertion(logRecord).apply(block).check()
        }
    }
}
