package com.showsrecommendations.reviews.adapters.controllers

import com.showsrecommendations.reviews.domain.usecases.AddReview


class ReviewController(private val addReview: AddReview) {
    fun addReview(userId: String, showId: String, rating: Float): AddReview.Response {
        val addReviewRequest = AddReview.Request(
            userId = userId,
            showId = showId,
            rating = rating)
        return addReview.invoke(addReviewRequest = addReviewRequest)
    }
}