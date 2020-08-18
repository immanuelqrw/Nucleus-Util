package com.immanuelqrw.core.util

import org.amshove.kluent.shouldBeNull
import org.amshove.kluent.shouldEqual
import org.junit.jupiter.api.Test

internal class SetExtensionsTest {

    @Test
    fun minTest() {
        val set = sortedSetOf(1, 10, 12, 0, -1)
        -1 shouldEqual set.min()

        val emptySet = sortedSetOf<Int>()
        emptySet.min().shouldBeNull()
    }

    @Test
    fun maxTest() {
        val set = sortedSetOf(1, 10, 12, 0, -1)
        12 shouldEqual set.max()

        val emptySet = sortedSetOf<Int>()
        emptySet.max().shouldBeNull()
    }

}
