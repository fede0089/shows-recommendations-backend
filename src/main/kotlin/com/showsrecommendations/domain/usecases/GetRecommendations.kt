package com.showsrecommendations.domain.usecases

import com.showsrecommendations.domain.entities.Recommendation
import com.showsrecommendations.domain.entities.User
import com.showsrecommendations.domain.ports.RecommendationsRepository

class GetRecommendations(private val recommendationsRepository: RecommendationsRepository) {

    //TODO remove seen shows
    operator fun invoke(userId: String): List<Recommendation> =
        recommendationsRepository.getRecommendations(userId = userId)
            .sortedBy { it.unrecommendationsQty - it.recommendationsQty }
}