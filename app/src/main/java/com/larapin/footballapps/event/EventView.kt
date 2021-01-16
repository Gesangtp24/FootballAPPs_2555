package com.larapin.footballapps.event

import com.larapin.footballapps.model.Event

interface EventView{
    fun showLoading()
    fun hideLoading()
    fun showEventList(data: List<Event>)
}