package com.immanuelqrw.core.util

/**
 * Executes [block] iff this (result of previous method) is true.
 *
 * @param block
 * @return Given Boolean
 * */
inline fun Boolean.whenTrue(block: () -> Unit): Boolean {
    if (this) block()
    return this
}

/**
 * Executes [block] iff this (result of previous method) is false
 *
 * @param block
 * @return Given Boolean
 * */
inline fun Boolean.whenFalse(block: () -> Unit): Boolean {
    if (!this) block()
    return this
}
