package com.github.shynixn.petblocks.rx.contract

import com.github.shynixn.petblocks.rx.entity.SkinPage
import org.bukkit.entity.Player

/**
 * The BrowserService connects to web pages.
 */
interface BrowserService {
    /**
     * Opens the start page of the browser.
     */
    fun openMainPage(player: Player)

    /**
     * Clears all actions the player has requested.
     */
    fun clearActions(player: Player)

    /**
     * Opens the list of the given [skinPage].
     */
    fun openListPage(player: Player, skinPage: SkinPage)
}