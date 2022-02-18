package com.showsrecommendations.domain.ports

import com.showsrecommendations.domain.entities.Recommendation

interface RecommendationsRepository {
    fun getRecommendations(userId: String): List<Recommendation>
}