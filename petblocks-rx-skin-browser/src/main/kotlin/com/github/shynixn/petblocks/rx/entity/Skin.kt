package com.github.shynixn.petblocks.rx.entity

/**
 * Skin.
 */
class Skin(
    var id: Int,
    var damageValue: Int,
    var skinUrl: String = "",
    var name: String = "",
    val lore: MutableList<String> = ArrayList()
)