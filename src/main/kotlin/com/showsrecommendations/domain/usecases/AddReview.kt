package com.showsrecommendations.domain.usecases

import com.showsrecommendations.domain.ports.ReviewsRepository
import io.ktor.features.*

class AddReview(private val reviewsRepository: ReviewsRepository): UseCase<AddReview.Request, AddReview.Response>{

    override fun invoke(addReviewRequest: Request): Response {

        val review = reviewsRepository.getReview(addReviewRequest.userId, addReviewRequest.showId)

        if(review != null){
            throw AlreadyReviewedShowException()
        }

        reviewsRepository.addReview(userId = addReviewRequest.userId,
            showId = addReviewRequest.showId,
            rating = addReviewRequest.rating)

        return Response(
            userId = addReviewRequest.userId,
            showId = addReviewRequest.showId,
            rating = addReviewRequest.rating)
    }

    data class Request(val userId: String, val showId: String, val rating:Float)
    data class Response(val userId: String, val showId: String, val rating:Float)
    class AlreadyReviewedShowException: BadRequestException(message = "Show was already reviewed")
}

