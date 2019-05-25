package com.github.shynixn.petblocks.rx.contract

import com.github.shynixn.petblocks.rx.entity.ClickResult
import com.github.shynixn.petblocks.rx.entity.Skin
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

/**
 * MinecraftGUI Service.
 */
interface MinecraftGUIService : AutoCloseable {
    /**
     * Opens the gui for the given [player]. Does nothing if the gui is already open.
     */
    fun openGui(player: Player)

    /**
     * Clears the gui window from items.
     */
    fun clearGui(player : Player)

    /**
     * Returns if the given [inventory] matches the inventory of this service.
     */
    fun isGUIInventory(inventory: Inventory): Boolean

    /**
     * Handles clicking  on an [item] at the given [relativeSlot] and returning a [ClickResult].
     */
    fun clickOnItem(player: Player, relativeSlot: Int, item: ItemStack): ClickResult<*>

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