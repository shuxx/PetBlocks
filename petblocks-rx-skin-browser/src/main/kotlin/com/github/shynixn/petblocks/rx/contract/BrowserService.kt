package com.github.shynixn.petblocks.rx.contract

import org.bukkit.entity.Player

interface BrowserService {
    /**
     * Opens the start page of the browser.
     */
    fun openMainPage(player: Player)
}