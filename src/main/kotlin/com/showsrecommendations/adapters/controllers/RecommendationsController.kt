package com.showsrecommendations.adapters.controllers

import com.showsrecommendations.adapters.controllers.dtos.RecommendedShowDTO
import com.showsrecommendations.domain.usecases.CalculateAndGetRecommendations
import com.showsrecommendations.domain.usecases.GetRecommendations
import com.showsrecommendations.domain.usecases.GetShow

class RecommendationsController(private val getRecommendations: GetRecommendations, private val getShow: GetShow, private val calculateAndGetRecommendations: CalculateAndGetRecommendations) {

    fun getRecommendedShows(userId:String): List<RecommendedShowDTO> =
        getRecommendations(getRecommendationsRequest = GetRecommendations.Request(userId = userId))
            .map{recommendation ->
                val recommendedShow: GetShow.Response = getShow(GetShow.Request(showId = recommendation.showId))
                RecommendedShowDTO.from(recommendation = recommendation, show = recommendedShow)
            }

    fun calculateAndGetRecommendedShows(userId:String): List<RecommendedShowDTO> =
        calculateAndGetRecommendations(userId = userId)
            .map{recommendation ->
                val recommendedShow: GetShow.Response = getShow(GetShow.Request(showId = recommendation.showId))
                RecommendedShowDTO.from(recommendation = recommendation, show = recommendedShow)
            }
}