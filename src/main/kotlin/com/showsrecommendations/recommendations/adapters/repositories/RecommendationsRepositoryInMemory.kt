package com.showsrecommendations.recommendations.adapters.repositories

import com.showsrecommendations.recommendations.domain.entities.Recommendation
import com.showsrecommendations.reviews.domain.entities.Review
import com.showsrecommendations.users.domain.ports.FollowedUsersRepository
import com.showsrecommendations.recommendations.domain.ports.RecommendationsRepository
import com.showsrecommendations.reviews.domain.ports.ReviewsRepository

class RecommendationsRepositoryInMemory(
    private val followedUsersRepository: FollowedUsersRepository,
    private val reviewsRepository: ReviewsRepository
)
    : RecommendationsRepository {

    override fun getRecommendations(userId: String): List<Recommendation> {
        val followedUsersReviews = followedUsersRepository.getFollowedUsers(userId).
            flatMap{followedUser -> reviewsRepository.getReviews(followedUser.id)}

        return followedUsersReviews
            .groupBy {review: Review -> review.showId}
            .mapNotNull { (showId, reviews) ->
                val positiveReviewsQty = reviews.count { review: Review -> review.rating == 1f }
                val negativeReviewsQty = reviews.count() - positiveReviewsQty
                Recommendation(showId = showId, positiveReviewsQty = positiveReviewsQty, negativeReviewsQty = negativeReviewsQty)
            }
    }

    override fun getRecommendation(userId: String, showId: String): Recommendation? =
        getRecommendations(userId).find { it.showId == showId }
}