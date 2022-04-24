package com.showsrecommendations.adapters.controllers

import com.showsrecommendations.domain.usecases.GetRecommendations

class RecommendationsController(private val getRecommendations: GetRecommendations) {

    fun getRecommendedShows(userId:String): GetRecommendations.Response {
        val getRecommendationsRequest = GetRecommendations.Request(userId = userId)
        return getRecommendations(getRecommendationsRequest = getRecommendationsRequest)
    }

}