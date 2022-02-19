package com.showsrecommendations.domain.ports

import com.showsrecommendations.domain.entities.Review

interface ReviewsRepository {
    fun getReviews(userId: String): List<Review>
}