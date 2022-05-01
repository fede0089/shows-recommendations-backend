package com.showsrecommendations.reviews.adapters.controllers

import com.showsrecommendations.reviews.domain.usecases.AddReview
import com.showsrecommendations.reviews.domain.usecases.UndoReview


class ReviewController(private val addReview: AddReview, private val undoReview: UndoReview) {
    fun addReview(userId: String, showId: String, rating: Float): AddReview.Response {
        val request = AddReview.Request(
            userId = userId,
            showId = showId,
            rating = rating)
        return addReview.invoke(request = request)
    }

    fun undoReview(userId: String, showId: String): UndoReview.Response {
        val request = UndoReview.Request(
            userId = userId,
            showId = showId)
        return undoReview.invoke(request = request)
    }
}