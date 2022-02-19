package com.showsrecommendations.domain.usecases

import com.showsrecommendations.domain.entities.Recommendation
import com.showsrecommendations.domain.entities.Review
import com.showsrecommendations.domain.ports.ReviewsRepository
import com.showsrecommendations.domain.ports.UsersRepository

class CalculateAndGetRecommendations(private val usersRepository: UsersRepository, private val reviewsRepository: ReviewsRepository) {

    operator fun invoke(userId: String): List<Recommendation> {

        val seenShows = reviewsRepository.getReviews(userId).map {it.showId}

        val followedUsersReviews = usersRepository.getFollowedUsers(userId).
            flatMap{followedUser -> reviewsRepository.getReviews(followedUser.id)}

        val followedUsersRecommendations = followedUsersReviews
            .groupBy {review: Review -> review.showId}
            .map { (showId, reviews) ->

                val positiveReviewsQty = reviews.count { review: Review -> review.rating == 1f }
                val negativeReviewsQty = reviews.count() - positiveReviewsQty

                Recommendation(showId = showId,
                    positiveReviewsQty = positiveReviewsQty,
                    negativeReviewsQty = negativeReviewsQty)
        }

        return followedUsersRecommendations
            .sortedBy {recommendation-> recommendation.negativeReviewsQty - recommendation.positiveReviewsQty}
            .filterNot{recommendation -> recommendation.showId in seenShows}
    }
}