package com.showsrecommendations.recommendations.domain.ports

import com.showsrecommendations.recommendations.domain.entities.Recommendation

interface RecommendationsRepository {
    fun getRecommendations(userId: String): List<Recommendation>
    fun getRecommendation(userId: String, showId: String): Recommendation?
}