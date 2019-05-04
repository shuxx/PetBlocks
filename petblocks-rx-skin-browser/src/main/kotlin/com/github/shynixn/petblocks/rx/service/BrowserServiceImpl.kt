package com.github.shynixn.petblocks.rx.service

import com.github.shynixn.petblocks.api.business.service.LoggingService
import com.github.shynixn.petblocks.rx.contract.BrowserService
import com.github.shynixn.petblocks.rx.contract.MinecraftGUIService
import com.github.shynixn.petblocks.rx.entity.Skin
import com.github.shynixn.petblocks.rx.extension.AsyncMinecraftThread
import com.github.shynixn.petblocks.rx.extension.UIMinecraftThread
import com.google.inject.Inject
import io.reactivex.Observable
import org.bukkit.entity.Player
import org.jsoup.Jsoup

class BrowserServiceImpl @Inject constructor(private val loggingService: LoggingService, private val minecraftGUIService: MinecraftGUIService) : BrowserService {
    private val hostname = "https://minecraft-heads.com"

    /**
     * Opens the start page of the browser.
     */
    override fun openMainPage(player: Player) {
        val observable = Observable.fromCallable {
            val document = Jsoup.connect(hostname).get()

            document.body().allElements.filter { e ->
                e.tagName() == "a" && e.children().size > 0
                        && e.child(0).tagName() == "span"
            }.filter { e -> e.attr("href") == "/custom-heads" || e.attr("href") == "/player-heads" }
                .map { e ->
                    val skin = Skin(397, 3, hostname + e.attr("href"), e.child(0).child(0).attr("src"), e.child(0).childNode(2).toString().trim())
                    skin.lore.add("Change page")
                    skin
                }
        }

        observable.subscribeOn(AsyncMinecraftThread.toScheduler())
            .observeOn(UIMinecraftThread.toScheduler())
            .retry(5)
            .subscribe({ skins ->
                skins[1].skinUrl =
                    "http://textures.minecraft.net/texture/d5c6dc2bbf51c36cfc7714585a6a5683ef2b14d47d8ff714654a893f5da622"

                minecraftGUIService.setItem(player, 21, skins[0])
                minecraftGUIService.setItem(player, 23, skins[1])
            }, { error ->
                loggingService.error("Failed to retrieve elemetns", error)
            }, {
                println("COMPLETED.")
            })
    }
}