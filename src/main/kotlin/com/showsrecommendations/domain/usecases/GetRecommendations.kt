package com.showsrecommendations.domain.usecases

import com.showsrecommendations.domain.entities.Recommendation
import com.showsrecommendations.domain.ports.RecommendationsRepository
import com.showsrecommendations.domain.ports.ReviewsRepository

class GetRecommendations(private val recommendationsRepository: RecommendationsRepository,
                         private val reviewsRepository: ReviewsRepository): UseCase<GetRecommendations.Request, List<Recommendation>> {

    override operator fun invoke(getRecommendationsRequest: Request): List<Recommendation> {
        val seenShows = reviewsRepository.getReviews(getRecommendationsRequest.userId).map { it.showId }
        return recommendationsRepository.getRecommendations(userId = getRecommendationsRequest.userId)
            .filterNot { it.showId in seenShows }
            .sortedBy { it.negativeReviewsQty - it.positiveReviewsQty }
    }

    data class Request(val userId:String)
}