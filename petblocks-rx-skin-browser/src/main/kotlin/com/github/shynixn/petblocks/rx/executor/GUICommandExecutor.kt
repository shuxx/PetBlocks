package com.github.shynixn.petblocks.rx.executor

import com.github.shynixn.petblocks.api.business.command.PlayerCommand
import com.github.shynixn.petblocks.rx.contract.BrowserService
import com.github.shynixn.petblocks.rx.contract.MinecraftGUIService
import com.github.shynixn.petblocks.rx.entity.Skin
import com.google.inject.Inject
import org.bukkit.entity.Player

/**
 * Created by Shynixn 2019.
 * <p>
 * Version 1.2
 * <p>
 * MIT License
 * <p>
 * Copyright (c) 2019 by Shynixn
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
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