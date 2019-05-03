package com.github.shynixn.petblocks.rx.service

import com.github.shynixn.petblocks.api.business.enumeration.ChatColor
import com.github.shynixn.petblocks.rx.contract.MinecraftGUIService
import com.github.shynixn.petblocks.rx.entity.Skin
import com.github.shynixn.petblocks.rx.extension.displayName
import com.github.shynixn.petblocks.rx.extension.skin
import net.minecraft.server.v1_13_R2.ChatMessage
import net.minecraft.server.v1_13_R2.PacketPlayOutOpenWindow
import org.bukkit.Material
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import kotlin.math.roundToInt

class MinecraftGUIServiceImpl : MinecraftGUIService {
    /**
     * Sets the skin at the given [index]of the gui of the [player].
     */
    override fun setItem(player: Player, index: Int, skin: Skin) {
        var itemStack = ItemStack(Material.getMaterial("SKULL_HEAD"), 3)

        itemStack.displayName = skin.name
        itemStack.skin = skin.downloadUrl

        player.openInventory.topInventory.setItem(index, itemStack)
    }

    /**
     * Changes the header of the gui of the [player] to the given [message].
     */
    override fun setHeader(player: Player, message: String) {
        val targetMessage = ChatMessage(ChatColor.translateChatColorCodes('&', message))
        val nmsPlayer = (player as CraftPlayer).handle

        nmsPlayer.playerConnection.sendPacket(
            PacketPlayOutOpenWindow(
                nmsPlayer.activeContainer.windowId,
                "minecraft:chest",
                targetMessage,
                player.getOpenInventory().getTopInventory().getSize()
            )
        )
        nmsPlayer.updateInventory(nmsPlayer.activeContainer)
    }

    /**
     * Changes the percentage [percent] of the gui of the [player] of the progressbar.
     */
    override fun setProgressBarPercentage(player: Player, percent: Double) {
        val amountOfDisplayedSlots = (percent / 9.0).roundToInt()

        for (i in 0 until amountOfDisplayedSlots) {
            player.openInventory.topInventory.setItem(i, ItemStack(Material.EMERALD_BLOCK))
        }

        for (i in 0 until 9 - amountOfDisplayedSlots) {
            player.openInventory.topInventory.setItem(i, ItemStack(Material.REDSTONE_BLOCK))
        }
    }

}