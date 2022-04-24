package com.showsrecommendations.adapters.controllers
import com.showsrecommendations.domain.usecases.GetShow

class ShowController(private val getShow:GetShow) {
    fun getShow(showId: String): GetShow.Response {
        val getShowRequest = GetShow.Request(showId = showId)
        return getShow.invoke(getShowRequest = getShowRequest)
    }
}