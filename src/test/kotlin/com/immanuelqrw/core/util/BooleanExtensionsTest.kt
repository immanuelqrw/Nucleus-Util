package com.immanuelqrw.core.util

import org.junit.jupiter.api.Test
import kotlin.test.fail

internal class BooleanExtensionsTest {

    @Test
    fun `given true - when whenTrue block executed - then execute`() {
        true.whenTrue { return }
        fail()
    }

    @Test
    fun `given false - when whenTrue block executed - then do not execute`() {
        false.whenTrue { fail() }
    }

    @Test
    fun `given false - when whenFalse block executed - then execute`() {
        false.whenFalse { return }
        fail()
    }

    @Test
    fun `given false - when whenFalse block executed - then do not execute`() {
        true.whenFalse { fail() }
    }

}
