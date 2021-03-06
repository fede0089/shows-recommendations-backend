package com.showsrecommendations.shows.domain.usecases

import com.showsrecommendations.recommendations.domain.ports.RecommendationsRepository
import com.showsrecommendations.reviews.domain.ports.ReviewsRepository
import com.showsrecommendations.shows.domain.ports.ShowsRepository
import com.showsrecommendations.UseCase
import io.ktor.server.plugins.*

class GetShowForLoggedUser(
    private val showsRepository: ShowsRepository,
    private val recommendationsRepository: RecommendationsRepository,
    private val reviewsRepository: ReviewsRepository
): UseCase<GetShowForLoggedUser.Request, GetShowForLoggedUser.Response> {

    override operator fun invoke(getShowRequest: Request): Response {
        val show = showsRepository.getShow(getShowRequest.showId) ?: throw NotFoundException(message = "Show not found")
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
            userReview = userReview?.let{ UserReview(rating = it.rating, createdDate = it.createdDate)},
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
                        val userReview: UserReview?,
                        val positiveReviewsQty:Int,
                        val negativeReviewsQty: Int)

    data class UserReview(val rating:Float,
                          val createdDate:String)
}