package com.immanuelqrw.core.util

import org.amshove.kluent.shouldEqual
import org.junit.jupiter.api.Test
import java.util.Random
import kotlin.test.fail

internal class MapExtensionsTest {

    @Test
    fun testGetWeightedRandom() {
        val items = mapOf(Pair("-1", 0.0), Pair("0", 0.5), Pair("1", 0.75), Pair("2", 0.5), Pair("3", 0.25))
        val rand = SettableRandom()
        rand.setNextDoubleValues(0.0, 0.2, 0.6, 0.25, 0.9999, 0.85, 0.9)
        "0" shouldEqual items.getWeightedRandom(rand) // withMessage "-1 should be never selected shouldEqual it has weight 0.0"
        "0" shouldEqual items.getWeightedRandom(rand)
        "1" shouldEqual items.getWeightedRandom(rand)
        "1" shouldEqual items.getWeightedRandom(rand)
        "3" shouldEqual items.getWeightedRandom(rand)
        "2" shouldEqual items.getWeightedRandom(rand)
        "3" shouldEqual items.getWeightedRandom(rand)
        try {
            repeat(100) { items.getWeightedRandom(Random()) }
        } catch (e: Exception) {
            fail("this method should not throw with default random class. Exception is $e")
        }
    }

    @Test
    fun testGetKeysInWeightedRandomOrder() {
        val items = mapOf(Pair("-1", 0.0), Pair("0", 0.5), Pair("1", 0.75), Pair("2", 0.5), Pair("3", 0.25))
        val rand = SettableRandom()
        rand.setNextDoubleValues(0.9999, 0.5, 0.2, 0.45, 0.99)
        val expected = listOf("0", "3", "2", "1", "-1")
        items.getKeysInWeightedRandomOrder(0.0, rand) shouldEqual expected
    }

    @Test
    fun testGetNoWeightsRandom() {
        val items = mapOf(Pair("0", 0.0), Pair("1", 0.0))
        val rand = SettableRandom()
        rand.setNextIntValues(0, 1)
        "0" shouldEqual items.getWeightedRandom(rand)
        "1" shouldEqual items.getWeightedRandom(rand)
    }

    @Test
    fun testMergeReduce() {
        val map1 = mapOf("a" to 5, "b" to 10, "c" to 11, "d" to 3)
        val map2 = mapOf("a" to 3, "c" to 8, "d" to 3, "e" to 4)
        val reduction = map1.mergeReduce(map2)
        mapOf("a" to 5, "b" to 10, "c" to 11, "d" to 3, "e" to 4) shouldEqual reduction
        val sumReduction = map1.mergeReduce(map2) { a, b -> a - b }
        mapOf("a" to 2, "b" to 10, "c" to 3, "d" to 0, "e" to 4) shouldEqual sumReduction
    }

    @Test
    fun testMergeReduceTo() {
        val map1 = mapOf("a" to 5, "b" to 10, "c" to 11, "d" to 3)
        val map2 = mapOf("a" to 3, "c" to 8, "d" to 3, "e" to 4)
        val reduction: Map<String, Int> = map1.mergeReduceTo(LinkedHashMap(), map2)
        mapOf("a" to 5, "b" to 10, "c" to 11, "d" to 3, "e" to 4) shouldEqual reduction
        val sumReduction: Map<String, Int> = map1.mergeReduceTo(LinkedHashMap(), map2) { a, b -> a - b }
        mapOf("a" to 2, "b" to 10, "c" to 3, "d" to 0, "e" to 4) shouldEqual sumReduction
    }


    @Test
    fun testJoin() {
        val map1 = mapOf("a" to 5, "b" to 10, "c" to 11)
        val map2 = mapOf("a" to "a", "c" to "b", "d" to "c")
        val reduction = map1.join(map2) { v1, v2 -> Pair(v1 ?: 100, v2 ?: "z") }
        mapOf("a" to Pair(5, "a"), "b" to Pair(10, "z"), "c" to Pair(11, "b"), "d" to Pair(100, "c")) shouldEqual reduction
    }

    @Test
    fun testSwapKeys() {
        val map = mapOf(
            "a" to mapOf(1 to 5.0, 2 to 2.0, 3 to 5.0),
            "b" to mapOf(2 to 7.0, 5 to 9.0),
            "c" to mapOf(1 to 2.0, 4 to 2.0),
            "d" to mapOf(4 to 9.0, 5 to 5.0)
        )
        val swapped = map.swapKeys().toSortedMap()
        val expected = mapOf(
            1 to mapOf("a" to 5.0, "c" to 2.0),
            2 to mapOf("a" to 2.0, "b" to 7.0),
            3 to mapOf("a" to 5.0),
            4 to mapOf("c" to 2.0, "d" to 9.0),
            5 to mapOf("b" to 9.0, "d" to 5.0)
        )
        swapped shouldEqual expected
    }

    @Test
    fun testSwap3Keys() {
        val map = mapOf(
            "a" to mapOf(1 to mapOf(4.5 to false, 4.2 to true)),
            "b" to mapOf(8 to mapOf(1.5 to false, 7.2 to true)),
            "c" to mapOf(9 to mapOf(8.8 to true, 4.2 to false))
        )

        val swappedK2WithK1 = map.swapKeys { k1, k2, k3 -> Triple(k2, k1, k3) }


        val expectedK2withK1 = mapOf(
            1 to mapOf("a" to mapOf(4.5 to false, 4.2 to true)),
            8 to mapOf("b" to mapOf(1.5 to false, 7.2 to true)),
            9 to mapOf("c" to mapOf(8.8 to true, 4.2 to false))
        )
        expectedK2withK1 shouldEqual swappedK2WithK1

        val swappedK3withK1 = map.swapKeys { k1, k2, k3 -> Triple(k3, k2, k1) }

        val expectedK3withK1 = mapOf(
            4.5 to mapOf(1 to mapOf("a" to false)),
            4.2 to mapOf(
                1 to mapOf("a" to true),
                9 to mapOf("c" to false)
            ),
            1.5 to mapOf(8 to mapOf("b" to false)),
            7.2 to mapOf(8 to mapOf("b" to true)),
            8.8 to mapOf(9 to mapOf("c" to true))
        )

        expectedK3withK1 shouldEqual swappedK3withK1
    }


    @Test
    fun testToTwoDimensionalMap() {
        val map = mapOf(
            Pair(1, "a") to 5,
            Pair(1, "b") to 3,
            Pair(1, "c") to 8,
            Pair(2, "a") to 12,
            Pair(2, "d") to 14,
            Pair(4, "z") to 7
        )
        val twoLevels = map.toTwoLevelMap()
        val expected = mapOf(
            1 to mapOf("a" to 5, "b" to 3, "c" to 8),
            2 to mapOf("a" to 12, "d" to 14),
            4 to mapOf("z" to 7)
        )
        twoLevels shouldEqual expected
    }

    @Test
    fun testListToTwoDimensionalMap() {
        val map = listOf(
            Pair(1, "a") to 5,
            Pair(1, "b") to 3,
            Pair(1, "c") to 8,
            Pair(2, "a") to 12,
            Pair(2, "d") to 14,
            Pair(4, "z") to 7
        )
        val twoLevels = map.toTwoLevelMap()
        val expected = mapOf(
            1 to mapOf("a" to 5, "b" to 3, "c" to 8),
            2 to mapOf("a" to 12, "d" to 14),
            4 to mapOf("z" to 7)
        )
        twoLevels shouldEqual expected
    }

    @Test
    fun testToThreeDimensionalMap() {
        val map = mapOf(
            Triple(1, "a", 2.0) to 5,
            Triple(1, "b", 1.0) to 3,
            Triple(1, "b", 2.0) to 8,
            Triple(2, "a", 1.0) to 12,
            Triple(2, "a", 2.0) to 14,
            Triple(2, "a", 3.0) to 7
        )
        val twoLevels = map.toThreeLevelMap()
        val expected = mapOf(
            1 to mapOf("a" to mapOf(2.0 to 5), "b" to mapOf(1.0 to 3, 2.0 to 8)),
            2 to mapOf("a" to mapOf(1.0 to 12, 2.0 to 14, 3.0 to 7))
        )
        twoLevels shouldEqual expected
    }

    @Test
    fun testGetSecondLevelValues() {
        val map = mapOf(
            "a" to mapOf(1 to 5.0, 2 to 2.0, 3 to 5.0),
            "b" to mapOf(2 to 7.0, 5 to 9.0),
            "c" to mapOf(1 to 2.0, 4 to 2.0),
            "d" to mapOf(4 to 9.0, 5 to 5.0)
        )
        val expected = setOf(5.0, 2.0, 7.0, 9.0)
        map.getSecondLevelValues() shouldEqual expected
    }

    @Test
    fun testGetThirdLevelValues() {
        val map = mapOf(
            "a" to mapOf(1 to mapOf(4.5 to 1, 4.2 to 1)),
            "b" to mapOf(8 to mapOf(1.5 to 2, 7.2 to 1)),
            "c" to mapOf(9 to mapOf(8.8 to 4, 4.2 to 3))
        )
        val expected = setOf(1, 2, 3, 4)
        map.getThirdLevelValues() shouldEqual expected
    }

    @Test
    fun testMerge() {
        val map1 = mapOf(1 to "a", 2 to "b", 3 to "c")
        val map2 = mapOf(2 to "d", 3 to "c", 4 to "f")
        val map3 = mapOf(2 to "x", 3 to "y", 4 to "z")
        val expected = mapOf(1 to "a".asList(), 2 to listOf("b", "d", "x"), 3 to listOf("c", "c", "y"), 4 to listOf("f", "z"))
        listOf(map1, map2, map3).merge() shouldEqual expected
    }

    @Test
    fun testFlatMerge() {
        val map1 = mapOf(1 to listOf("a", "b"), 2 to listOf("b"), 3 to listOf("c"))
        val map2 = mapOf(2 to listOf("d"), 3 to listOf("c", "d", "e"), 4 to listOf("f"))
        val map3 = mapOf(2 to listOf("x"), 3 to listOf("y"), 4 to listOf("z", "k"))
        val expected = mapOf(1 to listOf("a", "b"), 2 to listOf("b", "d", "x"), 3 to listOf("c", "c", "d", "e", "y"), 4 to listOf("f", "z", "k"))
        listOf(map1, map2, map3).flatMerge() shouldEqual expected
    }

}
