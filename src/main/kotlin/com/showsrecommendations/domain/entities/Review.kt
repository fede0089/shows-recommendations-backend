package com.showsrecommendations.domain.entities

data class Review(
    val showId: String,
    val rating: Float,
    val createdDate: String
)