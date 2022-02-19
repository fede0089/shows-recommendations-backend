package com.showsrecommendations.domain.usecases

import com.showsrecommendations.domain.entities.Recommendation
import com.showsrecommendations.domain.ports.RecommendationsRepository
import com.showsrecommendations.domain.ports.ReviewsRepository

class GetRecommendations(private val recommendationsRepository: RecommendationsRepository, private val reviewsRepository: ReviewsRepository) {

    operator fun invoke(userId: String): List<Recommendation> {
        val seenShows = reviewsRepository.getReviews(userId).map { it.showId }
        return recommendationsRepository.getRecommendations(userId = userId)
            .filterNot { it.showId in seenShows }
            .sortedBy { it.unrecommendationsQty - it.recommendationsQty }
    }
}