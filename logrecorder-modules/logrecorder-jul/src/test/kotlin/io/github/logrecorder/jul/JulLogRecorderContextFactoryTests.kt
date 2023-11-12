package io.github.logrecorder.jul

import io.github.logrecorder.common.LogRecorderContextFactory
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.util.ServiceLoader

internal class JulLogRecorderContextFactoryTests {

    @Test
    fun `service is registered correctly`() {
        val cut = ServiceLoader.load(LogRecorderContextFactory::class.java).single()
        assertThat(cut).isInstanceOf(JulLogRecorderContextFactory::class.java)
    }
}
