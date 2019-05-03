package com.github.shynixn.petblocks.rx.service

import com.github.shynixn.petblocks.rx.entity.Skin
import io.reactivex.Observable
import org.bukkit.entity.Player
import org.jsoup.nodes.Document

interface BrowserService {


    /**
     * Lets the given [player] subscribe to the skin browser.
     */
    fun subscribe(player : Player) : Observable<Document>


    fun unsubscribe(player : Player)





}