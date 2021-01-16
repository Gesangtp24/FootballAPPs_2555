package com.larapin.footballapps.detail.eventDetail

import com.larapin.footballapps.model.detailEvent

interface EventDetailView{
    fun showLoading()
    fun hideLoading()
    fun showEventDetail(data: List<detailEvent>)
}