package info.novatec.testit.logrecorder.logback

import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.Appender
import ch.qos.logback.core.Context
import ch.qos.logback.core.filter.Filter
import ch.qos.logback.core.spi.FilterReply
import ch.qos.logback.core.status.Status

internal class CallbackAppender(
    private var name: String,
    private val eventConsumer: (ILoggingEvent) -> Unit
) : Appender<ILoggingEvent> {

    override fun getName(): String {
        return name
    }

    override fun setName(name: String) {
        this.name = name
    }

    override fun doAppend(event: ILoggingEvent) {
        eventConsumer(event)
    }

    override fun setContext(context: Context) {

    }

    override fun getContext(): Context? {
        return null
    }

    override fun addStatus(status: Status) {

    }

    override fun addInfo(msg: String) {

    }

    override fun addInfo(msg: String, ex: Throwable) {

    }

    override fun addWarn(msg: String) {

    }

    override fun addWarn(msg: String, ex: Throwable) {

    }

    override fun addError(msg: String) {

    }

    override fun addError(msg: String, ex: Throwable) {

    }

    override fun addFilter(newFilter: Filter<ILoggingEvent>) {

    }

    override fun clearAllFilters() {

    }

    override fun getCopyOfAttachedFiltersList(): List<Filter<ILoggingEvent>>? {
        return null
    }

    override fun getFilterChainDecision(event: ILoggingEvent): FilterReply? {
        return null
    }

    override fun start() {

    }

    override fun stop() {

    }

    override fun isStarted(): Boolean {
        return false
    }

}
