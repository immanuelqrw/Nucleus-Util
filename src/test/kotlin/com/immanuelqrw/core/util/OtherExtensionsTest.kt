package com.immanuelqrw.core.util

import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeFalse
import org.amshove.kluent.shouldBeNull
import org.amshove.kluent.shouldBeTrue
import org.amshove.kluent.shouldEqual
import org.amshove.kluent.shouldNotBeNull
import org.junit.jupiter.api.Test
import java.util.Optional
import java.util.UUID
import kotlin.test.assertFails
import kotlin.test.fail

internal class OtherExtensionsTest {

    @Test
    fun kClassTest() {
        String::class shouldEqual kClass()
        List::class shouldEqual kClass()

        // it is not possible to test other cases since generics are always erased in the runtime
    }

    @Test
    fun propagateNullTest() {
        val allNullPair: Pair<String?, Int?> = null to null
        allNullPair.propagateNull().shouldBeNull()

        val leftNullPair: Pair<String?, Int?> = null to 0
        leftNullPair.propagateNull().shouldBeNull()

        val rightNullPair: Pair<String?, Int?> = "null" to null
        rightNullPair.propagateNull().shouldBeNull()

        val notNullPair: Pair<String?, Int?> = "null" to 0
        val actual = notNullPair.propagateNull()

        actual.shouldNotBeNull()
        notNullPair shouldEqual actual
    }

    @Test
    fun orNullWithValue() {
        val storedValue = 1.0
        val optional = Optional.of(storedValue)
        storedValue shouldEqual optional.orNull()
    }

    @Test
    fun orNullWithNull() {
        val optional = Optional.empty<Int>()
        optional.orNull().shouldBeNull()
    }

    @Test
    fun testWhenNull() {
        val notNull: Int? = 1
        notNull.whenNull { fail("Value is not null, this thing should not be called") } shouldEqual 1 // "Original value should be returned."

        val nonNullable = 1
        nonNullable.whenNull { fail("Value is not null, this thing should not be called") } shouldEqual 1 // "Original value should be returned."

        var response = false
        val value: Int? = null
        value.whenNull { response = true }.shouldBeNull()
        response.shouldBeTrue()
    }

    @Test
    fun testAsList() {
        listOf(1) shouldEqual 1.asList()
        val nullable: Int? = null
        nullable?.asList().shouldBeNull()
    }

    @Test
    fun testIntersect() {
        val range1 = 1..5
        val range2 = 5..7
        val range3 = 0..1
        val range4 = 6..7
        val range5 = 0..0

        range1.intersects(range2).shouldBeTrue()
        range1.intersects(range3).shouldBeTrue()

        range1.intersects(range4).shouldBeFalse()
        range1.intersects(range5).shouldBeFalse()
    }

    @Test
    fun `with test`() {
        val a = 1
        val b = 3
        val result = a with b
        2 shouldEqual result.size

        a shouldEqual result[0]
        b shouldEqual result[1]
    }

    @Test
    fun `validate test`() {
        val txt = "hello world"
        assertFails {
            txt.validate(false) { fail() }
        }

        assertFails {
            txt.validate(isValidSelector = { it == "$txt wrong!" }, invalidBlock = { fail() })
        }

        txt.validate(true) { fail() }
        txt.validate(isValidSelector = { it == txt }, invalidBlock = { fail() })
    }

    @Test
    fun `test applyIf applied`() {
        val listUnderTest = mutableListOf(1)

        listUnderTest.applyIf(true) { set(0, 0) } shouldBe listUnderTest
        0 shouldEqual listUnderTest[0]
    }

    @Test
    fun `test applyIf not applied()`() {
        val listUnderTest = mutableListOf(1)

        listUnderTest.applyIf(false) { set(0, 0) } shouldBe listUnderTest
        1 shouldEqual listUnderTest[0]
    }

    @Test
    fun `test applyIf via block applied`() {
        val listUnderTest = mutableListOf(1)

        listUnderTest.applyIf(shouldApplyBlock = {
            it shouldBe listUnderTest
            true
        }, block = { set(0, 0) }) shouldBe listUnderTest
        0 shouldEqual listUnderTest[0]
    }

    @Test
    fun `test applyIf via block not applied()`() {
        val listUnderTest = mutableListOf(1)

        listUnderTest.applyIf(shouldApplyBlock = {
            it shouldBe listUnderTest
            false
        }, block = { set(0, 0) }) shouldBe listUnderTest
        1 shouldEqual listUnderTest[0]
    }

    @Test
    fun toLongString() {
        // With default parameters
        "Double(42.0)" shouldEqual 42.0.toLongString("42.0")
        // With explicit parameters
        "Any object".toLongString("short", "[]", className = "MyClass") shouldEqual "MyClass[short]"
    }

    @Test
    fun toShortString() {
        val short = "SHORT"
        val long = short.toLongString(short)
        short shouldEqual long.toShortString()
    }

    @Test
    fun `test isURL valid urls`() {
        val inputs = (0..5).map { "http://hello.com/$it" } + listOf("https://gooo.pw/a#b", "https://sm.ai:90")
        inputs.forEach { isURL(it).whenFalse { org.junit.jupiter.api.fail("Method did not recognize correctly $it! This is valid URL") } }
    }

    /**
     * Test with examples of urls that are false positives cases for the [isURL] function.
     */
    @Test
    fun isUrlFalsePositives() {
        val inputs = listOf("https://sm.ai,", "https://,", "https:/sm.ai/sd", "https:sm.ai/sd")
        inputs.forEach { isURL(it).whenFalse { org.junit.jupiter.api.fail("Method did recognize correctly $it, however it is expected to fail") } }
    }

    @Test
    fun `test isURL invalid urls`() {
        val inputs = (0..5).map { it.toString() } + listOf("gooo.pw", "ht://", "https//:sm.ai/sd", UUID.randomUUID().toString())
        inputs.forEach { isURL(it).whenTrue { org.junit.jupiter.api.fail("Method did not recognize correctly $it. This is not valid URL.") } }
    }

    @Test
    fun `test isUUID with valid inputs`() {
        val inputs = (0..5).map { UUID.randomUUID() } + UUID(0L, 0L)
        inputs.forEach { isUUID(it.toString()).whenFalse { org.junit.jupiter.api.fail("Method did recognize correctly $it, it is valid UUID.") } }
    }

    @Test
    fun `test isUUID with invalid inputs`() {
        val inputs = (0..5).map { it.toString() } + listOf("ajksdjkfjkgf", "", "sdfghfg", Long.toString())
        inputs.forEach { isUUID(it).whenTrue { org.junit.jupiter.api.fail("Method did recognize correctly $it, it is not valid UUID.") } }
    }

    @Test
    fun `getEnv returns same value as System getenv`() {
        val wellKnownExistingEnvVariable = "PATH"
        getEnv(wellKnownExistingEnvVariable) shouldEqual System.getenv(wellKnownExistingEnvVariable)
    }

    @Test
    fun `test newLine returns System lineSeparator`() {
        System.lineSeparator() shouldEqual newLine
    }

}
