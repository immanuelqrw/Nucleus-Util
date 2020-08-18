package com.immanuelqrw.core.util

import org.amshove.kluent.shouldEqual
import org.junit.jupiter.api.Test

internal class SequenceExtensionsTest {

    @Test
    fun testAssoc() {
        val input = sequenceOf(1 to 2, 3 to 4, 3 to 5)
        val expected = mapOf(1 to 2, 3 to 5)
        input.assoc() shouldEqual expected
    }

    @Test
    fun testAssocPair() {
        val input = sequenceOf(1 to 2, 3 to 4, 3 to 5)
        val expected = mapOf(1 to 2, 3 to 5)
        input.assoc { it } shouldEqual expected
    }

    @Test
    fun testAssocByKey() {
        val input = sequenceOf(1 to 2, 3 to 4, 3 to 5)
        val expected = mapOf(1 to Pair(1, 2), 3 to Pair(3, 5))
        input.assocBy { it.first } shouldEqual expected
    }

    @Test
    fun testAssocBy() {
        val input = sequenceOf(1 to 2, 3 to 4, 3 to 5)
        val expected = mapOf(1 to 2, 3 to 5)
        input.assocBy({ it.first }, { it.second }) shouldEqual expected
    }

    @Test
    fun testAssocWith() {
        val input = sequenceOf(1 to 2, 3 to 5, 3 to 5)
        val expected = mapOf(Pair(1, 2) to 2, Pair(3, 5) to 5)
        input.assocWith { it.second } shouldEqual expected
    }

}
