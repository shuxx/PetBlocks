package com.github.shynixn.petblocks.rx.contract

import com.github.shynixn.petblocks.rx.entity.Skin
import org.bukkit.entity.Player

interface MinecraftGUIService {
    /**
     * Changes the header of the gui of the [player] to the given [message].
     */
    fun setHeader(player: Player, message: String)

    /**
     * Changes the percentage [percent] of the gui of the [player] of the progressbar.
     */
    fun setProgressBarPercentage(player: Player, percent: Double)

    /**
     * Sets the skin at the given [index]of the gui of the [player].
     */
    fun setItem(player: Player, index: Int, skin: Skin)
}