package com.immanuelqrw.core.util

import org.amshove.kluent.shouldEqual
import org.junit.jupiter.api.Test
import java.time.Instant

internal class InstantExtensionsTest {

    private fun assertDuration(aEpoch: Long, bEpoch: Long, expected: Long) {
        val a = Instant.ofEpochMilli(aEpoch)
        val b = Instant.ofEpochMilli(bEpoch)

        durationInMilli(a, b) shouldEqual expected
    }

    @Test
    fun `durationInMilli same values test`() {
        assertDuration(100, 100, 0)
    }

    @Test
    fun `durationInMilli bigger to smaller test`() {
        assertDuration(1000, 100, 900)
    }

    @Test
    fun `durationInMilli smaller to bigger test`() {
        assertDuration(100, 101, 1)
    }

    private fun assertDurationTo(aEpoch: Long, bEpoch: Long, expected: Long) {
        val a = Instant.ofEpochMilli(aEpoch)
        val b = Instant.ofEpochMilli(bEpoch)
        a.durationToInMilli(b) shouldEqual expected
    }

    @Test
    fun `durationToInMilli same values test`() {
        assertDurationTo(100, 100, 0)
    }

    @Test
    fun `durationToInMilli bigger to smaller test`() {
        assertDurationTo(1000, 100, 900)
    }

    @Test
    fun `durationToInMilli smaller to bigger test`() {
        assertDurationTo(100, 101, 1)
    }

}
