package com.showsrecommendations.adapters.controllers

import com.showsrecommendations.adapters.controllers.dtos.RecommendedShowDTO
import com.showsrecommendations.domain.entities.Show
import com.showsrecommendations.domain.usecases.GetRecommendations
import com.showsrecommendations.domain.usecases.GetShow

class RecommendationsController(private val getRecommendations: GetRecommendations, private val getShow: GetShow) {

    fun getRecommendedShows(userId:String): List<RecommendedShowDTO> =
        getRecommendations(userId = userId)
            .map{recommendation ->
                val recommendedShow: Show = getShow(recommendation.showId)
                RecommendedShowDTO.from(recommendation = recommendation, show = recommendedShow)
            }
}