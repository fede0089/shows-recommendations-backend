package com.showsrecommendations.domain.ports

import com.showsrecommendations.domain.entities.Review

interface ReviewsRepository {
    fun getReview(userId: String, showId: String): Review?
    fun getReviews(userId: String): List<Review>
    fun addReview(userId: String, showId: String, rating: Float)
}