package com.github.shynixn.petblocks.rx.service

import com.github.shynixn.petblocks.api.business.service.LoggingService
import com.github.shynixn.petblocks.rx.contract.BrowserService
import com.github.shynixn.petblocks.rx.contract.MinecraftGUIService
import com.github.shynixn.petblocks.rx.entity.Skin
import com.github.shynixn.petblocks.rx.entity.SkinPage
import com.github.shynixn.petblocks.rx.extension.AsyncMinecraftThread
import com.github.shynixn.petblocks.rx.extension.UIMinecraftThread
import com.google.inject.Inject
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import org.bukkit.entity.Player
import org.jsoup.Jsoup
import java.util.concurrent.TimeUnit

/**
 * The BrowserServiceImpl handles WebRequests to the https://minecraft-heads.com website in order
 * to display skins in a gui.
 */
class BrowserServiceImpl @Inject constructor(private val loggingService: LoggingService, private val minecraftGUIService: MinecraftGUIService) : BrowserService {
    private val hostname = "https://minecraft-heads.com"

    private val actions = HashMap<Player, ArrayList<Disposable>>()
    private var pageObservables = HashMap<String, Observable<Pair<Int, Skin>>>()

    /**
     * Opens the start page of the browser.
     */
    override fun openMainPage(player: Player) {
        clearActions(player)

        val animatedMessage = displayAnimatedLoadingMessage(player)
        val observable = Observable.fromCallable {
            val document = Jsoup.connect(hostname).get()

            document.body().allElements.filter { e ->
                e.tagName() == "a" && e.children().size > 0
                        && e.child(0).tagName() == "span"
            }.filter { e -> e.attr("href") == "/custom-heads" || e.attr("href") == "/player-heads" }
                .map { e ->
                    val skin = Skin(397, 3, e.child(0).child(0).attr("src"), e.child(0).childNode(2).toString().trim())
                    skin.lore.add("Change page")
                    skin
                }
        }

        actions[player]!!.add(observable.subscribeOn(AsyncMinecraftThread.toScheduler())
            .observeOn(UIMinecraftThread.toScheduler())
            .retry(5)
            .subscribe({ skins ->
                skins[1].skinUrl =
                    "http://textures.minecraft.net/texture/d5c6dc2bbf51c36cfc7714585a6a5683ef2b14d47d8ff714654a893f5da622"

                minecraftGUIService.setItem(player, 21, skins[0])
                minecraftGUIService.setItem(player, 23, skins[1])
            }, { error ->
                animatedMessage.dispose()
                minecraftGUIService.setHeader(player, "Failed to connect.")
                loggingService.error("Failed to retrieve elements.", error)
            }, {
                animatedMessage.dispose()
                minecraftGUIService.setHeader(player, "Finished")
            }))
    }

    /**
     * Opens the list of the given [skinPage].
     */
    override fun openListPage(player: Player, skinPage: SkinPage) {
        clearActions(player)

        minecraftGUIService.clearGui(player)

        val url = hostname + "/" + skinPage.category
        val animatedMessage = displayAnimatedLoadingMessage(player)
        val observable = getListObservable(url)

        actions[player]!!.add(observable.subscribeOn(AsyncMinecraftThread.toScheduler())
            .observeOn(UIMinecraftThread.toScheduler())
            .retry(5)
            .subscribe({ result ->
                minecraftGUIService.setItem(player, result.first, result.second)
            }, { error ->
                animatedMessage.dispose()
                minecraftGUIService.setHeader(player, "Failed to retrieve elements.")
                loggingService.error("Failed to retrieve elements.", error)
            }, {
                animatedMessage.dispose()
                minecraftGUIService.setHeader(player, "Finished")
            }))
    }

    /**
     * Clears all actions the player has requested.
     */
    override fun clearActions(player: Player) {
        if (!actions.containsKey(player)) {
            actions[player] = ArrayList()
        }

        for (action in actions[player]!!) {
            action.dispose()
        }
    }

    /**
     * Gets the observable to load the skin list.
     */
    private fun getListObservable(url: String): Observable<Pair<Int, Skin>> {
        if (this.pageObservables.containsKey(url)) {
            return pageObservables[url]!!
        }

        pageObservables[url] = Observable.create<Pair<Int, Skin>> { emitter ->
            val document = Jsoup.connect(url).get()

            document.body().allElements.filter { e -> e.attr("class") == "itemList" }
                .forEach { e ->
                    var counter = 0
                    e.children().forEach { child ->
                        if (counter < 54) {
                            val detailPageUrl = hostname + child.children()[0].attr("href")
                            val detailDocument = Jsoup.connect(detailPageUrl).get()

                            val skinUrl = if (detailPageUrl.contains("player-heads")) {
                                detailDocument.getElementById("ACC-Name").html()

                            } else {
                                detailDocument.getElementById("UUID-Value").html()
                            }

                            val skin = Skin(397, 3, skinUrl)

                            // Push the next skin to the GUI.
                            emitter.onNext(Pair(counter, skin))

                            counter++
                        }
                    }
                }

            emitter.onComplete()
        }.cache() // Cache the results of the observable.

        return pageObservables[url]!!
    }

    /**
     * Displays a moving header text in the browser window of the [player].
     * @return A disposable message.
     */
    private fun displayAnimatedLoadingMessage(player: Player): Disposable {
        var counter = 0
        var progressBarGoingRight = true

        val disposable = Observable.fromCallable {
            val messageBuilder = StringBuilder()

            for (i in 0 until counter) {
                messageBuilder.append(" ")
            }

            messageBuilder.append(">> Loading page <<")

            minecraftGUIService.setHeader(player, messageBuilder.toString())

            if (progressBarGoingRight) {
                counter++
            } else {
                counter--
            }

            if (counter > 15 || counter <= 0) {
                progressBarGoingRight = !progressBarGoingRight
            }
        }.delay(10, TimeUnit.MILLISECONDS).subscribeOn(AsyncMinecraftThread.toScheduler()).repeat().subscribe()

        actions[player]!!.add(disposable)

        return disposable
    }
}