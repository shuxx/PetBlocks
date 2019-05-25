package com.github.shynixn.petblocks.rx.listener

import com.github.shynixn.petblocks.api.business.service.PersistencePetMetaService
import com.github.shynixn.petblocks.rx.contract.BrowserService
import com.github.shynixn.petblocks.rx.contract.MinecraftGUIService
import com.github.shynixn.petblocks.rx.entity.ClickResultApplySkin
import com.github.shynixn.petblocks.rx.entity.ClickResultLoadSkins
import com.google.inject.Inject
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerQuitEvent

/**
 * Listener for GUI events.
 */
class GUIListener @Inject constructor(
    private val guiService: MinecraftGUIService,
    private val browserService: BrowserService,
    private val petMetaService: PersistencePetMetaService
) : Listener {
    /**
     * Gets called from [Bukkit] and handles action to the inventory.
     */
    @EventHandler
    fun playerClickInInventoryEvent(event: InventoryClickEvent) {
        val player = event.whoClicked as Player

        if (!guiService.isGUIInventory(event.inventory)) {
            return
        }

        if (event.currentItem == null || event.currentItem!!.type == Material.AIR) {
            return
        }

        event.isCancelled = true
        player.updateInventory()

        val clickResult = guiService.clickOnItem(player, event.slot, event.currentItem!!)

        if (clickResult is ClickResultApplySkin) {
            val skin = clickResult.payload

            val petMeta = petMetaService.getPetMetaFromPlayer(player)
            petMeta.skin.typeName = skin.id.toString()
            petMeta.skin.dataValue = skin.damageValue
            petMeta.skin.sponsored = false
            petMeta.skin.unbreakable = false
            petMeta.skin.owner = skin.skinUrl
        }

        if (clickResult is ClickResultLoadSkins) {
            browserService.openListPage(player, clickResult.payload)
        }
    }

    /**
     * Gets called from [Bukkit] and handles cleaning up remaining resources.
     */
    @EventHandler
    fun playerQuitEvent(event: PlayerQuitEvent) {
        guiService.close()
    }
}