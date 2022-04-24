package com.showsrecommendations.domain.usecases
import com.showsrecommendations.domain.entities.Review
import com.showsrecommendations.domain.entities.Show
import com.showsrecommendations.domain.ports.FollowedUsersRepository
import com.showsrecommendations.domain.ports.ReviewsRepository
import com.showsrecommendations.domain.ports.ShowsRepository

class CalculateAndGetRecommendations(private val followedUsersRepository: FollowedUsersRepository,
                                     private val reviewsRepository: ReviewsRepository,
                                     private val showsRepository: ShowsRepository)
    : UseCase<CalculateAndGetRecommendations.Request, CalculateAndGetRecommendations.Response> {

    override operator fun invoke(calculateAndGetRecommendationsRequest: Request): Response {

        val seenShowsIds = reviewsRepository.getReviews(calculateAndGetRecommendationsRequest.userId).map {it.showId}

        val followedUsersReviews = followedUsersRepository.getFollowedUsers(calculateAndGetRecommendationsRequest.userId).
            flatMap{followedUser -> reviewsRepository.getReviews(followedUser.id)}

        val followedUsersRecommendations = followedUsersReviews
            .groupBy {review: Review -> review.showId}
            .mapNotNull { (showId, reviews) ->
                    val positiveReviewsQty = reviews.count { review: Review -> review.rating == 1f }
                    val negativeReviewsQty = reviews.count() - positiveReviewsQty
                    val show: Show = showsRepository.getShow(showId)
                    val recommendedShow = RecommendedShow(
                        title = show.title,
                        genres = show.genres,
                        year = show.year,
                        cover = show.cover,
                        type = show.type,
                        positiveReviewsQty = positiveReviewsQty,
                        negativeReviewsQty = negativeReviewsQty
                    )
                recommendedShow.takeIf { showId !in seenShowsIds }
            }
            .sortedBy { it.negativeReviewsQty - it.positiveReviewsQty }

        return Response(recommendations = followedUsersRecommendations)
    }

    data class Request(val userId:String)
    data class Response(val recommendations: List<RecommendedShow>)
    data class RecommendedShow (val title:String,
                                val genres: List<String>,
                                val year:String,
                                val cover:String,
                                val type:String,
                                val positiveReviewsQty:Int,
                                val negativeReviewsQty: Int)
}