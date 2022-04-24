package com.showsrecommendations.adapters.repositories.inmemory

import com.showsrecommendations.domain.entities.Recommendation
import com.showsrecommendations.domain.ports.RecommendationsRepository

class RecommendationsRepositoryInMemory(private val recommendations: MutableMap<String, List<Recommendation>> = mutableMapOf()): RecommendationsRepository, MutableMap<String, List<Recommendation>> by recommendations {
      override fun getRecommendations(userId: String): List<Recommendation> =
          recommendations.getOrDefault(userId, listOf())

    override fun getRecommendation(userId: String, showId: String): Recommendation? =
        getRecommendations(userId).find { it.showId == showId }
}