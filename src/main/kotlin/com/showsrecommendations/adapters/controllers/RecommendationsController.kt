package com.showsrecommendations.adapters.controllers

import com.showsrecommendations.domain.usecases.CalculateAndGetRecommendations
import com.showsrecommendations.domain.usecases.GetRecommendations

class RecommendationsController(private val getRecommendations: GetRecommendations,
                                private val calculateAndGetRecommendations: CalculateAndGetRecommendations) {

    fun getRecommendedShows(userId:String): GetRecommendations.Response {
        val getRecommendationsRequest = GetRecommendations.Request(userId = userId)
        return getRecommendations(getRecommendationsRequest = getRecommendationsRequest)
    }

    fun calculateAndGetRecommendedShows(userId:String): CalculateAndGetRecommendations.Response {
        val calculateAndGetRecommendationsRequest = CalculateAndGetRecommendations.Request(userId = userId)
        return calculateAndGetRecommendations(calculateAndGetRecommendationsRequest = calculateAndGetRecommendationsRequest)
    }

}