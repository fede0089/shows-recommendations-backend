package com.showsrecommendations.recommendations.adapters.controllers

import com.showsrecommendations.recommendations.domain.usecases.GetRecommendations

class RecommendationsController(private val getRecommendations: GetRecommendations) {

    fun getRecommendedShows(userId:String): GetRecommendations.Response {
        val getRecommendationsRequest = GetRecommendations.Request(userId = userId)
        return getRecommendations(getRecommendationsRequest = getRecommendationsRequest)
    }

}