package com.showsrecommendations.adapters.controllers

import com.showsrecommendations.domain.usecases.AddReview
import com.showsrecommendations.domain.usecases.GetShow

class ShowController(private val getShow:GetShow) {
    fun getShow(getShowRequest: GetShow.Request): GetShow.Response =
        getShow.invoke(getShowRequest = getShowRequest)
}