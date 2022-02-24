package com.showsrecommendations.adapters.repositories.inmemory

import com.showsrecommendations.domain.entities.Review
import com.showsrecommendations.domain.ports.ReviewsRepository
import java.time.LocalDateTime

class ReviewsRepositoryInMemory(private val reviews: MutableMap<String, MutableList<Review>> = mutableMapOf()): ReviewsRepository, MutableMap<String, MutableList<Review>> by reviews {

    override fun getReview(userId: String, showId: String): Review? =
        getReviews(userId).find { it.showId == showId }

    override fun getReviews(userId: String): List<Review> =
        reviews.getOrDefault(userId, mutableListOf())

    override fun addReview(userId: String, showId: String, rating: Float) {
        val now:String = LocalDateTime.now().toString()
        reviews
            .getOrPut(userId) { mutableListOf()}
            .add(Review(showId = showId, rating = rating, createdDate = now))
    }
}