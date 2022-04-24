package com.showsrecommendations.domain.usecases

import com.showsrecommendations.domain.ports.RecommendationsRepository
import com.showsrecommendations.domain.ports.ReviewsRepository
import com.showsrecommendations.domain.ports.ShowsRepository

class GetShowForLoggedUser(
    private val showsRepository: ShowsRepository,
    private val recommendationsRepository: RecommendationsRepository,
    private val reviewsRepository: ReviewsRepository
): UseCase<GetShowForLoggedUser.Request, GetShowForLoggedUser.Response> {

    override operator fun invoke(getShowRequest: Request): Response {
        val show = showsRepository.getShow(getShowRequest.showId)
        val recommendation = recommendationsRepository.getRecommendation(userId = getShowRequest.userId, showId = getShowRequest.showId)
        val userReview = reviewsRepository.getReview(userId = getShowRequest.userId, showId = getShowRequest.showId)
        return Response(
            id = show.id,
            title = show.title,
            genres = show.genres,
            year = show.year,
            cover = show.cover,
            type = show.type,
            overview = show.overview,
            userReview = userReview?.rating,
            positiveReviewsQty = recommendation?.positiveReviewsQty?:0,
            negativeReviewsQty = recommendation?.negativeReviewsQty?:0,
        )
    }

    data class Request(val userId:String, val showId: String)
    data class Response(val id:String,
                        val title: String,
                        val genres: List<String>,
                        val year:String,
                        val cover:String,
                        val type:String,
                        val overview:String,
                        val userReview: Float?,
                        val positiveReviewsQty:Int,
                        val negativeReviewsQty: Int)
}