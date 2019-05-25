package com.github.shynixn.petblocks.rx.service

import com.github.shynixn.petblocks.api.business.enumeration.ChatColor
import com.github.shynixn.petblocks.api.business.service.ItemService
import com.github.shynixn.petblocks.core.logic.business.extension.stripChatColors
import com.github.shynixn.petblocks.rx.contract.MinecraftGUIService
import com.github.shynixn.petblocks.rx.entity.*
import com.github.shynixn.petblocks.rx.extension.displayName
import com.github.shynixn.petblocks.rx.extension.skin
import com.google.inject.Inject
import net.minecraft.server.v1_13_R2.ChatMessage
import net.minecraft.server.v1_13_R2.PacketPlayOutOpenWindow
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import kotlin.math.roundToInt

/**
 * Minecraft GUI Service.
 */
class MinecraftGUIServiceImpl @Inject constructor(private val itemService: ItemService) : MinecraftGUIService {
    private val cache = HashMap<Player, GUICache>()

    /**
     * Opens the gui for the given [player]. Does nothing if the gui is already open.
     */
    override fun openGui(player: Player) {
        if (player.openInventory.topInventory.type == InventoryType.CHEST) {
            return
        }

        val inventory = Bukkit.getServer().createInventory(player, 54, "Booting...")
        cache[player] = GUICache(inventory)
        player.openInventory(inventory)

        clearGui(player)
    }

    /**
     * Clears the gui window from items.
     */
    override fun clearGui(player: Player) {
        for (i in 0 until 54) {
            val itemStack = itemService.createItemStack(160, 15).build<ItemStack>()
            player.openInventory.topInventory.setItem(i, itemStack)
        }
    }

    /**
     * Returns if the given [inventory] matches the inventory of this service.
     */
    override fun isGUIInventory(inventory: Inventory): Boolean {
        val holder = inventory.holder as? Player ?: return false

        return this.cache.containsKey(holder) && this.cache[holder]!!.inventory == inventory
    }

    /**
     * Sets the skin at the given [index]of the gui of the [player].
     */
    override fun setItem(player: Player, index: Int, skin: Skin) {
        openGui(player)

        val itemStack = itemService.createItemStack(skin.id, skin.damageValue).build<ItemStack>()

        itemStack.displayName = skin.name
        itemStack.skin = skin.skinUrl

        val meta = itemStack.itemMeta
        meta!!.lore = arrayListOf(skin.skinUrl)
        itemStack.itemMeta = meta

        if (index < 54) {
            player.openInventory.topInventory.setItem(index, itemStack)
        }
    }

    /**
     * Gets the target skin page a [player] requests by clicking on an [item] at the given [relativeSlot].
     */
    override fun clickOnItem(player: Player, relativeSlot: Int, item: ItemStack): ClickResult<*> {
        if (item.displayName.stripChatColors() == "Player heads") {
            return ClickResultLoadSkins(SkinPage("player-heads"))
        }

        if (item.displayName.stripChatColors() == "Custom heads") {
            return ClickResultLoadSkins(SkinPage("custom-heads"))
        }

        if (item.itemMeta!!.lore != null && item.itemMeta!!.lore!!.size == 1) {
            return ClickResultApplySkin(Skin(397, 3, item.itemMeta!!.lore!![0]))
        }

        return ClickResultApplySkin(Skin(397, 3, "Shynixn"))
    }

    /**
     * Changes the header of the gui of the [player] to the given [message].
     */
    override fun setHeader(player: Player, message: String) {
        openGui(player)

        val targetMessage = ChatMessage(ChatColor.translateChatColorCodes('&', message))
        val nmsPlayer = (player as CraftPlayer).handle

        nmsPlayer.playerConnection.sendPacket(
            PacketPlayOutOpenWindow(
                nmsPlayer.activeContainer.windowId,
                "minecraft:chest",
                targetMessage,
                player.getOpenInventory().topInventory.size
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

    /**
     * Closes this resource, relinquishing any underlying resources.
     * This method is invoked automatically on objects managed by the
     * `try`-with-resources statement.
     *
     * @throws Exception if this resource cannot be closed
     */
    override fun close() {
        cache.clear()
    }
}