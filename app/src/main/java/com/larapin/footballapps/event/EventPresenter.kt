package com.larapin.footballapps.event

import com.google.gson.Gson
import com.larapin.footballapps.api.repoApi
import com.larapin.footballapps.api.TheSportDBApi
import com.larapin.footballapps.model.responEvent
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class EventPresenter(private val view: EventView,
                     private val repoApi: repoApi,
                     private val gson: Gson){
    fun getEventList(league: String?, event: String?){
        view.showLoading()
        doAsync {
            val data = gson.fromJson(repoApi
                    .doRequest(TheSportDBApi.getEvent(league, event)),
                    responEvent::class.java
            )

            uiThread {
                view.hideLoading()
                view.showEventList(data.events)
            }
        }
    }
}
