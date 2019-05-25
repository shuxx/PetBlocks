package com.github.shynixn.petblocks.rx.entity

/**
 * Base ClickResult class.
 */
open class ClickResult<T>(
    /**
     * Payload of the click result.
     */
    val payload: T
)