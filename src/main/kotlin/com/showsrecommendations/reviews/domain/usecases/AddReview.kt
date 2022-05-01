package com.showsrecommendations.reviews.domain.usecases

import com.showsrecommendations.reviews.domain.ports.ReviewsRepository
import com.showsrecommendations.UseCase
import io.ktor.server.plugins.*

class AddReview(private val reviewsRepository: ReviewsRepository): UseCase<AddReview.Request, AddReview.Response> {

    override fun invoke(request: Request): Response {

        val review = reviewsRepository.getReview(request.userId, request.showId)

        if(review != null){
            throw AlreadyReviewedShowException()
        }

        reviewsRepository.addReview(userId = request.userId,
            showId = request.showId,
            rating = request.rating)

        return Response(
            showId = request.showId,
            rating = request.rating)
    }

    data class Request(val userId: String, val showId: String, val rating:Float)
    data class Response(val showId: String, val rating:Float)
    class AlreadyReviewedShowException: BadRequestException(message = "Show was already reviewed")
}

