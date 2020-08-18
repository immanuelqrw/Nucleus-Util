package com.immanuelqrw.core.util

import org.amshove.kluent.invoking
import org.amshove.kluent.shouldBeNull
import org.amshove.kluent.shouldBeTrue
import org.amshove.kluent.shouldEqual
import org.amshove.kluent.shouldThrow
import org.junit.jupiter.api.Test
import java.util.Random
import java.util.TreeSet
import kotlin.test.fail

internal class IterableExtensionsTest {

    @Test
    fun toNavigableSetTest() {
        val iterable = listOf(1, 2, 10, 0, 3, 3)
        val expected = TreeSet(iterable)
        val actual = iterable.toNavigableSet(Comparator.naturalOrder())

        actual shouldEqual expected
    }

    @Test
    fun testSumByLong() {
        val longs = listOf(10L, 20L, 30L)
        120L shouldEqual longs.sumByLong { it * 2 }
        val empty = listOf<Long>()
        0L shouldEqual empty.sumByLong { it * 2 }
    }

    @Test
    fun testSumByLongSequence() {
        val longs = sequenceOf(10L, 20L, 30L)
        120L shouldEqual longs.sumByLong { it * 2 }
        val empty = sequenceOf<Long>()
        0L shouldEqual empty.sumByLong { it * 2 }
    }

    @Test
    fun testGetRandomElement() {
        val items = listOf("0", "1", "2", "3", "4")
        val rand = SettableRandom()
        rand.setNextIntValues(0, 3, 2, 1, 4)
        "0" shouldEqual items.getRandomElement(rand)
        "3" shouldEqual items.getRandomElement(rand)
        "2" shouldEqual items.getRandomElement(rand)
        "1" shouldEqual items.getRandomElement(rand)
        "4" shouldEqual items.getRandomElement(rand)
        try {
            repeat(100) { items.getRandomElement(Random()) }
        } catch (e: Exception) {
            fail("this method should not throw with default random class. Exception is $e")
        }
    }

    @Test
    fun testReduction() {
        val values = listOf(1, 5, 7, 3, 1, 8)
        val cumSum = values.reduction(0) { a, b -> a + b }
        listOf(1, 6, 13, 16, 17, 25) shouldEqual cumSum
        val cumProd = values.reduction(1) { a, b -> a * b }
        listOf(1, 5, 35, 105, 105, 840) shouldEqual cumProd
    }

    @Test
    fun testSumByIndexes() {
        val empty: Iterable<List<Int>> = listOf()
        invoking { empty.sumByIndexes() } shouldThrow Exception::class

        val single = listOf(List(4) { it })
        single.first() shouldEqual single.sumByIndexes()

        val multiple = listOf(List(5) { 2 * it + 1 }, List(4) { it }, List(4) { 1 })
        val result = listOf(2, 5, 8, 11)
        result shouldEqual multiple.sumByIndexes()
    }

    @Test
    fun testMaxValueBy() {
        val empty = listOf<Int>()
        empty.maxValueBy { it }.shouldBeNull()

        val data = listOf(1 to "a", 5 to "c", 5 to "a", 0 to "l")
        5 shouldEqual data.maxValueBy { it.first }
        "l" shouldEqual data.maxValueBy { it.second }
    }

    @Test
    fun testMinValueBy() {
        val empty = listOf<Int>()
        empty.minValueBy { it }.shouldBeNull()

        val data = listOf(1 to "a", 5 to "c", 5 to "a", 0 to "l")
        0 shouldEqual data.minValueBy { it.first }
        "a" shouldEqual data.minValueBy { it.second }
    }

    @Test
    fun testDominantValueBy() {
        val empty = listOf<Int>()
        empty.dominantValueBy { it }.shouldBeNull()

        val data = listOf(1 to "a", 5 to "c", 5 to "a", 0 to "l")
        5 shouldEqual data.dominantValueBy { it.first }
        "a" shouldEqual data.minValueBy { it.second }
    }

    @Test
    fun testCartesianProduct() {
        val input1 = listOf(1, 2, 3)
        val input2 = listOf('a', 'b')
        val output = setOf(Pair(1, 'a'), Pair(1, 'b'), Pair(2, 'a'), Pair(2, 'b'), Pair(3, 'a'), Pair(3, 'b'))
        output shouldEqual input1.cartesianProduct(input2)
    }

    @Test
    fun testForEachNotNull() {
        val result = ArrayList<Int>()
        val input = listOf(1, 2, null, 3, 4, null)
        val expected = listOf(1, 2, 3, 4)
        input.forEachNotNull { result.add(it) }
        result shouldEqual expected
    }

    @Test
    fun testUnion() {
        val input1 = listOf(1, 2, 3)
        val input2 = listOf(3, 4, 5)
        val input3 = listOf(5, 6, 7)
        val output = setOf(1, 2, 3, 4, 5, 6, 7)
        listOf(input1, input2, input3).union() shouldEqual output
    }

    @Test
    fun testIntersect() {
        val input1 = listOf(1, 2, 3)
        val input2 = listOf(3, 4, 5)
        val input3 = listOf(5, 3, 7)
        val output = setOf(3)
        listOf(input1, input2, input3).intersect() shouldEqual output
    }

    @Test
    fun testFilterNotNullBy() {
        val input = listOf(1 to 2, null, 2 to null, 3 to 4, 5 to 6)
        val output = listOf(1 to 2, 3 to 4, 5 to 6)
        output shouldEqual input.filterNotNullBy { it.second }
    }

    @Test
    fun testSingleOrEmpty() {
        val input = listOf(1, 2, 3, 4, 5)
        5 shouldEqual input.singleOrEmpty { it > 4 }
        input.singleOrEmpty { it > 5 }.shouldBeNull()
        invoking { input.singleOrEmpty { it > 3 } } shouldThrow IllegalArgumentException::class

        1 shouldEqual listOf(1).singleOrEmpty()
        emptyList<Int>().singleOrEmpty().shouldBeNull()
        invoking { input.singleOrEmpty() } shouldThrow IllegalArgumentException::class
    }

    @Test
    fun testSplitPairCollection() {
        val input = listOf(1 to 2, 3 to 4, 5 to 6)
        val (odd, even) = input.splitPairCollection()
        listOf(1, 3, 5) shouldEqual odd
        listOf(2, 4, 6) shouldEqual even
    }

    @Test
    fun testSetDifferenceBy() {
        val `this` = listOf(1 to 1, 2 to 1, 3 to 1)
        val other = listOf(1 to 2, 2 to 2, 5 to 2)
        val result = `this`.setDifferenceBy(other) { it.first }
        (result.size == 1).shouldBeTrue()
        (result.single() == 3 to 1).shouldBeTrue()
    }

    @Test
    fun testAssoc() {
        val input = listOf(1 to 2, 3 to 4, 3 to 5)
        val expected = mapOf(1 to 2, 3 to 5)
        input.assoc() shouldEqual expected
    }

    @Test
    fun testAssocPair() {
        val input = listOf(1 to 2, 3 to 4, 3 to 5)
        val expected = mapOf(1 to 2, 3 to 5)
        input.assoc { it } shouldEqual expected
    }

    @Test
    fun testAssocByKey() {
        val input = listOf(1 to 2, 3 to 4, 3 to 5)
        val expected = mapOf(1 to Pair(1, 2), 3 to Pair(3, 5))
        input.assocBy { it.first } shouldEqual expected
    }

    @Test
    fun testAssocBy() {
        val input = listOf(1 to 2, 3 to 4, 3 to 5)
        val expected = mapOf(1 to 2, 3 to 5)
        input.assocBy({ it.first }, { it.second }) shouldEqual expected
    }

    @Test
    fun testAssocWith() {
        val input = listOf(1 to 2, 3 to 5, 3 to 5)
        val expected = mapOf(Pair(1, 2) to 2, Pair(3, 5) to 5)
        input.assocWith { it.second } shouldEqual expected
    }

    @Test
    fun testFlattenToLists() {
        val input = (1..9).map { Triple(it, it * 10, it * 100) }
        val expected = Triple((1..9).toList(), (1..9).map { it * 10 }, (1..9).map { it * 100 })

        input.flattenToLists() shouldEqual expected
    }
    @Test
    fun itemsToString() {
        val itemToString: (Int) -> String = {i -> "NUM$i"}
        val result = listOf(10, 20).itemsToString("numbers", itemToString = itemToString )
        "2 numbers: NUM10, NUM20" shouldEqual result
    }

}
