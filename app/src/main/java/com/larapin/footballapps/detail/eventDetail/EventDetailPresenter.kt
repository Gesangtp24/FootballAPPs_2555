package com.larapin.footballapps.detail.eventDetail

import com.google.gson.Gson
import com.larapin.footballapps.api.repoApi
import com.larapin.footballapps.api.TheSportDBApi
import com.larapin.footballapps.model.rd_Event
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class EventDetailPresenter(private val view: EventDetailView,
                           private val repoApi: repoApi,
                           private val gson: Gson){

    fun getEventDetail(eventId: String?){
        view.showLoading()
        doAsync {
            val data = gson.fromJson(repoApi
                    .doRequest(TheSportDBApi.getEventDetail(eventId)),
                    rd_Event::class.java
            )

            uiThread {
                view.hideLoading()
                view.showEventDetail(data.detailEvents)
            }
        }
    }
}