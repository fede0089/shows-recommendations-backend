package com.showsrecommendations.adapters.repositories.mocked

import com.showsrecommendations.domain.entities.Recommendation
import com.showsrecommendations.domain.ports.RecommendationsRepository

class RecommendationsRepositoryMocked(private val recommendations: Map<String, List<Recommendation>> = mapOf()): RecommendationsRepository {

      override fun getRecommendations(userId: String): List<Recommendation> =
          recommendations[userId]!!
}