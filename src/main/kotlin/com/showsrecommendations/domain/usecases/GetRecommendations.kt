package com.showsrecommendations.domain.usecases
import com.showsrecommendations.domain.entities.Show
import com.showsrecommendations.domain.ports.RecommendationsRepository
import com.showsrecommendations.domain.ports.ReviewsRepository
import com.showsrecommendations.domain.ports.ShowsRepository

class GetRecommendations(private val recommendationsRepository: RecommendationsRepository,
                         private val reviewsRepository: ReviewsRepository,
                         private val showsRepository: ShowsRepository)
    : UseCase<GetRecommendations.Request, GetRecommendations.Response> {

    override operator fun invoke(getRecommendationsRequest: Request): Response {
        val seenShows = reviewsRepository.getReviews(getRecommendationsRequest.userId).map { it.showId }
        val recommendations = recommendationsRepository.getRecommendations(userId = getRecommendationsRequest.userId)
            .filterNot { it.showId in seenShows }
            .map{
                val show: Show = showsRepository.getShow(it.showId)
                RecommendedShow(
                    id = show.id,
                    title = show.title,
                    genres = show.genres,
                    year = show.year,
                    cover = show.cover,
                    type = show.type,
                    positiveReviewsQty = it.positiveReviewsQty,
                    negativeReviewsQty = it.negativeReviewsQty
                )
            }
            .sortedBy { it.negativeReviewsQty - it.positiveReviewsQty }

        return Response(recommendations = recommendations)

    }

    data class Request(val userId:String)
    data class Response(val recommendations: List<RecommendedShow>)
    data class RecommendedShow (
        val id:String,
        val title:String,
        val genres: List<String>,
        val year:String,
        val cover:String,
        val type:String,
        val positiveReviewsQty:Int,
        val negativeReviewsQty: Int
    )
}