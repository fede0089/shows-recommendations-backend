package com.showsrecommendations.adapters.controllers

import com.showsrecommendations.domain.usecases.AddReview


class ReviewController(private val addReview: AddReview) {
    fun addReview(addReviewRequest:AddReview.Request): AddReview.Response =
        addReview.invoke(addReviewRequest = addReviewRequest)
}