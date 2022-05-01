package com.showsrecommendations.reviews.domain.usecases

import com.showsrecommendations.UseCase
import com.showsrecommendations.reviews.domain.ports.ReviewsRepository
import io.ktor.server.plugins.*

class UndoReview(private val reviewsRepository: ReviewsRepository): UseCase<UndoReview.Request, UndoReview.Response> {

    override fun invoke(request: Request):Response {
        val review = reviewsRepository.getReview(userId = request.userId, showId = request.showId)
            ?: throw NotReviewedShowException()
        reviewsRepository.undoReview(userId = request.userId, showId = request.showId)
        return Response(showId = request.showId, rating = review.rating, createdDate = review.createdDate)
    }

    data class Request(val userId: String, val showId: String)
    data class Response(val showId: String, val rating:Float, val createdDate: String)
    class NotReviewedShowException: BadRequestException(message = "The show was not reviewed by user")
}
