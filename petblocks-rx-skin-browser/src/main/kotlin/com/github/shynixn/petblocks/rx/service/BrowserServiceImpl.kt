package com.github.shynixn.petblocks.rx.service

import com.github.shynixn.petblocks.rx.entity.Skin
import com.github.shynixn.petblocks.rx.extension.AsyncMinecraftThread
import com.github.shynixn.petblocks.rx.extension.UIMinecraftThread
import io.reactivex.Observable
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class BrowserServiceImpl(private val plugin : Plugin) : BrowserService {
    /**
     * Lets the given [player] subscribe to the skin browser.
     */
    override fun subscribe(player: Player) : Observable<Document> {
        return Observable.fromCallable {
           Jsoup.connect("https://minecraft-heads.com").get()
        }
    }

    override fun unsubscribe(player: Player) {
        subscribe(player).subscribeOn(AsyncMinecraftThread.toScheduler())
            .observeOn(UIMinecraftThread.toScheduler())
            .retry(5)
            .subscribe({document ->
                val elements = document.body().allElements
            }, {

            },{

            })
    }
}