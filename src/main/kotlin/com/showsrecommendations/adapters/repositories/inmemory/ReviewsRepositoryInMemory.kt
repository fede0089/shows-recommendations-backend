package com.showsrecommendations.adapters.repositories.inmemory

import com.showsrecommendations.domain.entities.Review
import com.showsrecommendations.domain.ports.ReviewsRepository

class ReviewsRepositoryInMemory(private val reviews: MutableMap<String, List<Review>> = mutableMapOf()): ReviewsRepository, MutableMap<String, List<Review>> by reviews {
    override fun getReviews(userId: String): List<Review> =
        reviews[userId]!!
}