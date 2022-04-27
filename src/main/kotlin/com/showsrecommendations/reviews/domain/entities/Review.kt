package com.showsrecommendations.reviews.domain.entities

data class Review(
    val showId: String,
    val rating: Float,
    val createdDate: String
)