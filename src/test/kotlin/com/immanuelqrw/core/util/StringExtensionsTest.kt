package com.immanuelqrw.core.util

import org.amshove.kluent.shouldEqual
import org.junit.jupiter.api.Test

internal class StringExtensionsTest {

    @Test
    fun restrictLengthWithEllipsis() {
        val result = "ABCDEFGHIJK".restrictLengthWithEllipsis(8, "elip")
        "ABCDelip" shouldEqual result
    }

    @Test
    fun startsWithLetter(){
        val examples = mapOf("a lPha" to true, "Běta7" to true, "Čermák" to false, " delta" to false, "30" to false, "_" to false)
        for ( (key, value) in examples) {
            val result = key.startsWithLetter()
            result shouldEqual value // "The call \"$key\".startsWithLetter() should return $value"
        }
    }

}
