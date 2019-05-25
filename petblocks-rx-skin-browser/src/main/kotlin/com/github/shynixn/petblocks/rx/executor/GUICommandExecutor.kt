package com.github.shynixn.petblocks.rx.executor

import com.github.shynixn.petblocks.api.business.command.PlayerCommand
import com.github.shynixn.petblocks.rx.contract.BrowserService
import com.github.shynixn.petblocks.rx.contract.MinecraftGUIService
import com.google.inject.Inject
import org.bukkit.entity.Player

/**
 * The GUICommandExecutor handles opening the gui on certain commands.
 */
class GUICommandExecutor @Inject constructor(private val browserService: BrowserService, private val guiService: MinecraftGUIService) : PlayerCommand {
    /**
     * Gets called when the given [player] executes the defined command with the given [args].
     */
    override fun <P> onPlayerExecuteCommand(player: P, args: Array<out String>): Boolean {
        if (player !is Player) {
            throw IllegalArgumentException("Player has to be a BukkitPlayer!")
        }

        guiService.openGui(player)
        browserService.openMainPage(player)

        return true
    }
}