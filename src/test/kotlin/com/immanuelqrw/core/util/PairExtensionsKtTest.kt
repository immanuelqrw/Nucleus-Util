package com.immanuelqrw.core.util

import org.amshove.kluent.shouldEqual
import org.junit.jupiter.api.Test

internal class PairExtensionsKtTest {

    @Test
    fun `mapLeft test`() {
        val pair = Pair(listOf(10), 0)
        val expected = Pair(listOf(20), 0)
        pair.mapLeft { it * 2 } shouldEqual expected
    }

    @Test
    fun `mapRight test`() {
        val pair = Pair(0, listOf(10))
        val expected = Pair(0, listOf(20))
        pair.mapRight { it * 2 } shouldEqual expected
    }

    @Test
    fun `mapPair test`() {
        val pair = Pair(listOf(1), listOf(10))
        val expected = Pair(listOf("1"), listOf("20"))
        pair.mapPair({ it.toString() },  { (it * 2).toString() }) shouldEqual expected
    }

    @Test
    fun `letLeft test`() {
        val pair = Pair("hello", 0)
        val expected = Pair("hello world", 0)
        pair.letLeft { "$it world" } shouldEqual expected
    }

    @Test
    fun `letRight test`() {
        val pair = Pair("hello", 1)
        val expected = Pair("hello", 11)
        pair.letRight { it + 10 } shouldEqual expected
    }

    @Test
    fun `letPair test`() {
        val pair = Pair("hello", "world")
        val expected = Pair("hello 5", "wo")
        pair.letPair({ "$it 5" }, { it.take(2) }) shouldEqual expected
    }

}
